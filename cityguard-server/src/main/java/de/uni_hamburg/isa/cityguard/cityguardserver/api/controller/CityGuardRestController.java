package de.uni_hamburg.isa.cityguard.cityguardserver.api.controller;

import com.uber.h3core.util.LatLng;
import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.*;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.CategoryRepository;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.ReportRepository;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.Category;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.Report;
import de.uni_hamburg.isa.cityguard.cityguardserver.processing.SpatialIndexingService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * REST Controller for the CityGuard App.
 * It provides the following endpoints:
 * - GET /api/fetch_reports
 * - POST /api/submit_report
 * - GET /api/fetch_categories
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class CityGuardRestController {

	private final ReportRepository reportRepository;
	private final CategoryRepository categoryRepository;

	private final SpatialIndexingService spatialIndexingService;


	public CityGuardRestController(ReportRepository reportRepository, CategoryRepository categoryRepository, SpatialIndexingService spatialIndexingService) {
		this.reportRepository = reportRepository;
		this.categoryRepository = categoryRepository;
		this.spatialIndexingService = spatialIndexingService;
	}

	/**
	 * This endpoint fetches all reports in a given area and calculates a heatmap based on some reports.
	 * @param latitudeUpper The upper latitude of the area
	 * @param latitudeLower The lower latitude of the area
	 * @param longitudeLeft The left longitude of the area
	 * @param longitudeRight The right longitude of the area
	 * @return A JSON object containing a heatmap and a list of markers for all the data in the specified area
	 */
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/fetch_reports", produces = "application/json")
	public ReportVisualization fetchReports(
			@RequestParam Float latitudeUpper,
			@RequestParam Float latitudeLower,
			@RequestParam Float longitudeLeft,
			@RequestParam Float longitudeRight,
			@RequestParam List<Long> categories,
			@RequestParam Long heatmapCategory
	) {
		List<Report> selectedReports = reportRepository.findBetweenBounds(longitudeLeft, longitudeRight, latitudeLower, latitudeUpper, categories);
		List<MarkerVisualisation> markerReports = getMarkerVisualisations(selectedReports);

		List<Report> heatmapReports = reportRepository.findBetweenBounds(longitudeLeft, longitudeRight, latitudeLower, latitudeUpper, List.of(heatmapCategory));

		int resolution = spatialIndexingService.resolutionFromZoom(new LatLng(latitudeUpper, longitudeLeft), new LatLng(latitudeLower, longitudeRight));
		ReportVisualization reportVisualization = new ReportVisualization();
		List<HeatmapCell> heatmap = spatialIndexingService.calculateHeatmap(heatmapReports, resolution);

		List<HeatmapCell> heatmap2 = spatialIndexingService.calculateAllCells(
				resolution,
				new LatLon(latitudeUpper, longitudeLeft),
				new LatLon(latitudeUpper, longitudeRight),
				new LatLon(latitudeLower, longitudeRight),
				new LatLon(latitudeLower, longitudeLeft)
		);
		heatmap.addAll(heatmap2);
		reportVisualization.setHeatmap(heatmap);
		reportVisualization.setMarkers(markerReports);

		return reportVisualization;
	}

	private static List<MarkerVisualisation> getMarkerVisualisations(List<Report> selectedReports) {
		List<MarkerVisualisation> markerReports = new ArrayList<>();


		for (Report report : selectedReports) {
			if (report.getCategory().getAllowDiscrete()){
				MarkerVisualisation markerVisualisation = new MarkerVisualisation();
				markerVisualisation.setId(report.getId());
				markerVisualisation.setLatitude(report.getLatitude());
				markerVisualisation.setLongitude(report.getLongitude());
				markerVisualisation.setCategoryColor(report.getCategory().getColor());
				markerVisualisation.setCategoryIcon(report.getCategory().getIcon());
				markerReports.add(markerVisualisation);
			}
		}
		return markerReports;
	}


	/**
	 * This endpoint submits a report to the server.
	 * It takes a JSON object as input that is derived from the ReportForm class.
	 * @param reportForm The report to submit
	 * @return A JSON object containing the status of the request or an error message
	 */
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/submit_report",consumes = "application/json",produces = "application/json")
	public ResponseEntity<String> submitReports(@Valid @RequestBody ReportForm reportForm) {
		if (
				(!reportForm.getUseCurrentDateTime() && (reportForm.getReportedDate() == null || reportForm.getReportedTime() == null))
		) {
			return ResponseEntity.badRequest().body("Fehlerhafte Anfrage: Du musst ein Datum angeben");
		}
		if(categoryRepository.findById(reportForm.getCategoryId()).isEmpty()){
			return ResponseEntity.badRequest().body("Fehlerhafte Anfrage: Die angegebene Kategorie existiert nicht");
		}
		Report report = new Report();

		report.setLatitude(reportForm.getMeasured_latitude());
		report.setLongitude(reportForm.getMeasured_longitude());

		report.setCategory(categoryRepository.findById(reportForm.getCategoryId()).orElseThrow());
		report.setDescription(reportForm.getDescription());
		LocalDateTime dateTime = LocalDateTime.now();
		if (!reportForm.getUseCurrentDateTime()){
			dateTime = reportForm.getReportedDate().atTime(reportForm.getReportedTime());
		}
		report.setDateTime(dateTime);
		reportRepository.save(report);
		return ResponseEntity.ok("{\"status\": \"success\"}");
	}


	/**
	 * This endpoint fetches all categories from the database.
	 * A Report needs a category to be submitted so the client needs to know all categories.
	 * @return A JSON object containing all categories
	 */
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/fetch_categories",produces = "application/json")
	public List<Category> fetchCategories(){
		return categoryRepository.findAll();
	}

	/**
	 * This endpoint fetches a single report from the database.
	 * It is used to display a report in the detail view.
	 * @param customID The id of the report to fetch
	 * @return A JSON object containing the report
	 */
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/fetch_single_event_info",produces = "application/json")
	public ReportInformation fetchSingleReportInformation(@RequestParam Long customID) {
		ReportInformation reportInformation = new ReportInformation();
		Report report = reportRepository.findById(customID).orElseThrow();
		reportInformation.setCategory(report.getCategory().getName());
		reportInformation.setDescription(report.getDescription());
		LocalDateTime dateTime = report.getDateTime();
		String date = dateTime.getDayOfMonth() + "." + dateTime.getMonthValue() + "." + dateTime.getYear();
		String time = dateTime.getHour() + ":" + dateTime.getMinute();
		reportInformation.setDate(date);
		reportInformation.setTime(time);
		reportInformation.setCategoryColor(report.getCategory().getColor());
		reportInformation.setCategoryIcon(report.getCategory().getIcon());
		reportInformation.setTitle("Report #" + report.getId());
		return reportInformation;
}
}
