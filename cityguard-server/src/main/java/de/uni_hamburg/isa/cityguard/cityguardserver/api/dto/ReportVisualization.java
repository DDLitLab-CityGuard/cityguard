package de.uni_hamburg.isa.cityguard.cityguardserver.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * This class is used to serialize the data for the visualization of the reports
 * during an api call that fetches the reports.
 * That means that the fields of this class will be the fields of the JSON object
 */
@Getter
@Setter
public class ReportVisualization {
	private List<MarkerVisualisation> markers;
	private List<HeatmapCell> heatmap;
}
