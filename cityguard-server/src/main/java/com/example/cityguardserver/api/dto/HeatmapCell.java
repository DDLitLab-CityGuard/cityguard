package com.example.cityguardserver.api.dto;

import lombok.Getter;
import lombok.Setter;


/**
 * This class is used to serialize the data for the visualization of the reports
 * during an api call that fetches the reports.
 * It represents a cell of the heatmap in the ReportVisualization
 * That means that the fields of this class will be the fields of the JSON object
 */
@Getter
@Setter
public class HeatmapCell {
	private float latitude;
	private float longitude;
	private float sizeLat;
	private float sizeLon;
	private float value;
}
