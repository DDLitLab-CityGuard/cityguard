package com.example.cityguardserver;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CityGuardRestController {

    private final ReportRepository reportRepository;

    public CityGuardRestController(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/fetchreports")
    public List<Report> fetchreports(){
        return reportRepository.findAll();
    }
}
