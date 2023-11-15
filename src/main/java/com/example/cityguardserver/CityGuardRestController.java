package com.example.cityguardserver;

import com.example.cityguardserver.databasemodels.CategoryRepository;
import com.example.cityguardserver.databasemodels.Report;
import com.example.cityguardserver.databasemodels.ReportRepository;
import com.example.cityguardserver.forms.ReportForm;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CityGuardRestController {

    private final ReportRepository reportRepository;
    private final CategoryRepository categoryRepository;


    public CityGuardRestController(ReportRepository reportRepository, CategoryRepository categoryRepository) {
        this.reportRepository = reportRepository;
        this.categoryRepository = categoryRepository;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/fetchreports")
    public List<Report> fetchreports(){
        return reportRepository.findAll();
    }





    @CrossOrigin(origins = "*")
    @PostMapping(value = "/submit_report",consumes = "application/json",produces = "application/json")
    public void submitreports(@RequestBody ReportForm reportForm) {
        Report reportInstance= new Report();
        reportInstance.setCategory(reportForm.getCategory());
        reportInstance.setDescription(reportForm.getDescription());
        reportInstance.setLongitude(reportForm.getCoordinates().get(1));
        reportInstance.setLatitude(reportForm.getCoordinates().get(0));
        reportInstance.setCategory(reportForm.getCategory());
        reportInstance.setDateTime(String.valueOf(reportForm.getCalendar().getTime()));
        reportRepository.save(reportInstance);


    }
}



