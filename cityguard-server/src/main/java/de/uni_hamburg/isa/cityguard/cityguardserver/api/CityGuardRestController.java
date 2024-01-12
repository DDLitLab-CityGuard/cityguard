package de.uni_hamburg.isa.cityguard.cityguardserver.api;

import com.uber.h3core.util.LatLng;
import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.HeatmapCell;
import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.LatLon;
import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.ReportForm;
import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.ReportVisualization;
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
	 * This endpoint fetches all reports in a given area and calculates a heatmap based on some reports (the other reports st.
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
			@RequestParam Float longitudeRight
	) {
		List<Report> selectedReports = reportRepository.findBetweenBounds(longitudeLeft, longitudeRight, latitudeLower, latitudeUpper);
		List<Report> markerReports = new ArrayList<>(selectedReports.size());
		List<Report> heatmapReports = new ArrayList<>(selectedReports.size());

		for (Report report : selectedReports) {
			if (report.getCategory().getAllowDiscrete()){
				markerReports.add(report);
			}else{
				heatmapReports.add(report);
			}
		}

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
}
