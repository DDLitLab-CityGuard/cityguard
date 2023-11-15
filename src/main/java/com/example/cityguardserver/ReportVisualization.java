package com.example.cityguardserver;

import com.example.cityguardserver.databasemodels.Report;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ReportVisualization {
    private List<Report> markers;
    private List<HeatmapCell> heatmap;
}
