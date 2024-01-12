package de.uni_hamburg.isa.cityguard.cityguardserver.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * This class is used to serialize the data for the visualization of the reports
 * during an api call that fetches the reports.
 * It represents a cell of the heatmap in the ReportVisualization
 * That means that the fields of this class will be the fields of the JSON object
 */
@Getter
@Setter
public class HeatmapCell {
	private List<LatLon> polygon;
	private float value;
}
