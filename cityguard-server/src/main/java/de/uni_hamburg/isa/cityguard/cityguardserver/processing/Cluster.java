package de.uni_hamburg.isa.cityguard.cityguardserver.processing;

import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.LatLon;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.Category;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.Report;

import java.util.List;

public record Cluster(LatLon center, float score, Category category, List<Report> reportList) {
}
