package com.example.cityguardserver.api;


import com.example.cityguardserver.api.dto.HeatmapCell;
import com.example.cityguardserver.api.dto.ReportVisualization;
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

@RestController
@RequestMapping("/api")
public class CityGuardRestController {

    private final ReportRepository reportRepository;


    public CityGuardRestController(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/fetchreports", produces = "application/json")
    public ReportVisualization fetchreports(
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

            if (report.getHeatmap() == null || !report.getHeatmap()){
                markerReports.add(report);
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

    private float lonKmAtLatitude(float latitude) {
        float stabilizedLat = (float) (Math.floor(latitude * 10f) / 10f);
        return (float) (Math.cos(Math.toRadians(stabilizedLat)) * 111.32);
    }

    private float[] clipToKilometers(float lat, float lon, float kilometers, float referenceLatitude){
        float clippedLat = (float) (Math.floor(lat * (111f / kilometers)) / (111f / kilometers));
        float clippedLon = (float) (Math.floor(lon * (lonKmAtLatitude(referenceLatitude) / kilometers)) / (lonKmAtLatitude(referenceLatitude) / kilometers));
        return new float[]{clippedLat, clippedLon};
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/submit_report",consumes = "application/json",produces = "application/json")
    public ResponseEntity<String> submitReports(@Valid @RequestBody ReportForm reportForm) {
        if (
                (!reportForm.getUseCurrentDateTime() && (reportForm.getReportedDate() == null || reportForm.getReportedTime() == null))
        ) {
            return ResponseEntity.badRequest().body("Fehlerhafte Anfrage: Du musst ein Datum angeben");
        }
        Report report = new Report();
        report.setLatitude(reportForm.getLatitude());
        report.setLongitude(reportForm.getLongitude());
        report.setCategory(reportForm.getCategory());
        report.setDescription(reportForm.getDescription());
        LocalDateTime dateTime = LocalDateTime.now();
        if (!reportForm.getUseCurrentDateTime()){
            dateTime = reportForm.getReportedDate().atTime(reportForm.getReportedTime());
        }
        report.setDateTime(dateTime);
        System.out.println(report);
        reportRepository.save(report);
        return ResponseEntity.ok(":)");
    }
}



