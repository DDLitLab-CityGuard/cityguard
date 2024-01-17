package de.uni_hamburg.isa.cityguard.cityguardserver.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarkerVisualisation{
	private Long id;
	private Float longitude;
	private Float latitude;
	private String categoryIcon;
	private String categoryColor;
}
