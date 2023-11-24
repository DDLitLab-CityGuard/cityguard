package com.example.cityguardserver.api;


import com.example.cityguardserver.api.dto.HeatmapCell;
import com.example.cityguardserver.api.dto.ReportVisualization;
import com.example.cityguardserver.database.CategoryRepository;
import com.example.cityguardserver.database.dto.Category;
import com.example.cityguardserver.database.dto.Report;
import com.example.cityguardserver.database.ReportRepository;
import com.example.cityguardserver.api.dto.ReportForm;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for the CityGuard App.
 * It provides the following endpoints:
 * - GET /api/fetch_reports
 * - POST /api/submit_report
 * - GET /api/fetch_categories
 */
@RestController
@RequestMapping("/api")
public class CityGuardRestController {

    private final ReportRepository reportRepository;
    private final CategoryRepository categoryRepository;


    public CityGuardRestController(ReportRepository reportRepository, CategoryRepository categoryRepository) {
        this.reportRepository = reportRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * This endpoint fetches all reports in a given area and calculates a heatmap based on the reports.
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

        Map<Float, Map<Float, Float>> counter = new HashMap<>();
        List<Report> selectedReports = reportRepository.findBetweenBounds(longitudeLeft, longitudeRight, latitudeLower, latitudeUpper);
        List<Report> markerReports = new ArrayList<>();
        float size = 0.25f; //grid size in kilometers

        for (Report report : selectedReports) {

            if (report.getCategory().getAllowDiscrete()){
                markerReports.add(report);
                continue;
            }

            float[] clippedCords = clipToKilometers(report.getLatitude(), report.getLongitude(), size, latitudeUpper);
            increaseTileValue(counter, clippedCords[0], clippedCords[1], 1f);

            increaseTileValue(counter, clippedCords[0] - (size/111), clippedCords[1], 0.4f);
            increaseTileValue(counter, clippedCords[0] + (size/111), clippedCords[1], 0.4f);

            increaseTileValue(counter, clippedCords[0], clippedCords[1] + (size/lonKmAtLatitude(latitudeUpper)), 0.4f);
            increaseTileValue(counter, clippedCords[0], clippedCords[1] - (size/lonKmAtLatitude(latitudeUpper)), 0.4f);
        }

        ReportVisualization reportVisualization = new ReportVisualization();
        List<HeatmapCell> heatmap = new ArrayList<>();

        for(float lat : counter.keySet()){
            for(float lon : counter.get(lat).keySet()){
                HeatmapCell heatmapCell = new HeatmapCell();
                heatmapCell.setLatitude(lat);
                heatmapCell.setLongitude(lon);
                heatmapCell.setSizeLat(size/111f);
                heatmapCell.setSizeLon(size/lonKmAtLatitude(latitudeUpper));
                heatmapCell.setValue(Math.min(counter.get(lat).get(lon) * 0.2f, 0.6f));
                heatmap.add(heatmapCell);
            }
        }

        reportVisualization.setHeatmap(heatmap);
        reportVisualization.setMarkers(markerReports);

        return reportVisualization;
    }

    /*
     * This helper method increases the value of a tile in the heatmap during the calculation of the heatmap.
     */
    private void increaseTileValue(Map<Float, Map<Float, Float>> counter, float lat, float lon, float value){
        if(counter.containsKey(lat)) {
            if(counter.get(lat).containsKey(lon)) {
                counter.get(lat).put(lon, counter.get(lat).get(lon) + value);
            } else {
                counter.get(lat).put(lon, value);
            }
        } else {
            Map<Float, Float> newMap = new HashMap<>();
            newMap.put(lon, value);
            counter.put(lat, newMap);
        }
    }

    /*
     * This helper method calculates the length of a degree of longitude at a given latitude.
     */
    private float lonKmAtLatitude(float latitude) {
        float stabilizedLat = (float) (Math.floor(latitude * 10f) / 10f);
        return (float) (Math.cos(Math.toRadians(stabilizedLat)) * 111.32);
    }

    /*
     * This helper method clips a coordinate to a given grid size.
     * That means that the coordinate is rounded to the next multiple of the grid size.
     */
    private float[] clipToKilometers(float lat, float lon, float kilometers, float referenceLatitude){
        float clippedLat = (float) (Math.floor(lat * (111f / kilometers)) / (111f / kilometers));
        float clippedLon = (float) (Math.floor(lon * (lonKmAtLatitude(referenceLatitude) / kilometers)) / (lonKmAtLatitude(referenceLatitude) / kilometers));
        return new float[]{clippedLat, clippedLon};
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
        if(reportForm.getUseCurrentLocation()){
            report.setLatitude(reportForm.getMeasured_latitude());
            report.setLongitude(reportForm.getMeasured_longitude());
        }else{
            report.setLatitude(reportForm.getLatitude());
            report.setLongitude(reportForm.getLongitude());
        }
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



