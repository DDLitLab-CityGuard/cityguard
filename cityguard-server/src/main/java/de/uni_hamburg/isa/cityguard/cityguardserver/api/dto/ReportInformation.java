package de.uni_hamburg.isa.cityguard.cityguardserver.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportInformation {

	private String title;
	private String category;
	private String description;
	private String date;
	private String time;
	private String categoryType;
}
