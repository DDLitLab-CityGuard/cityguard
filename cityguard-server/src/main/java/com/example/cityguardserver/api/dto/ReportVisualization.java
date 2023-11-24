package com.example.cityguardserver.api.dto;

import com.example.cityguardserver.database.dto.Report;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ReportVisualization {
    private List<Report> markers;
    private List<HeatmapCell> heatmap;
}
