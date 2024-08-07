package de.uni_hamburg.isa.cityguard.cityguardserver.processing;

import com.uber.h3core.LengthUnit;
import com.uber.h3core.util.LatLng;
import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.HeatmapCell;
import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.LatLon;
import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.MarkerVisualisation;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.CategoryRepository;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.ReportRepository;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.Category;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ClusterAnalysisService {

	private final ReportRepository reportRepository;
	private final SpatialIndexingService spatialIndexingService;
	private final CategoryRepository categoryRepository;

	public List<HeatmapCell> generateHeatmapVisualization(
			Float latitudeUpper,
			Float latitudeLower,
			Float longitudeLeft,
			Float longitudeRight,
			Long heatmapCategory
	){
		List<Report> heatmapReports = reportRepository.findBetweenBounds(longitudeLeft, longitudeRight, latitudeLower, latitudeUpper, List.of(heatmapCategory));
		int resolution = 10;
		long bleedDistance = 0;
		Optional<Category> category = categoryRepository.findById(heatmapCategory);
		if(category.isPresent()){
			bleedDistance = category.get().getHeatmapSpreadRadius();
		}

		Map<String, List<Report>> addressMap = spatialIndexingService.groupByCell(heatmapReports, resolution);
		List<String> addressList = addressMap.keySet().stream().toList();


		Map<String, Float> scoreMap = new HashMap<>();
		for (String address : addressList){
			List<List<String>> addressRings = spatialIndexingService.addressBleed(address, (int) bleedDistance);
			for (int i = 0; i < addressRings.size(); i++) {
				List<String> ring = addressRings.get(i);
				for (String cellAddress : ring) {
					float score = scoreMap.getOrDefault(cellAddress, 0f);
					scoreMap.put(cellAddress, Math.max(score, ((-0.6f/(bleedDistance + 1f))*i)+0.6f));
				}
			}
		}

		List<HeatmapCell> heatmap = new ArrayList<>();
		for (String address : scoreMap.keySet()){
			HeatmapCell cell = new HeatmapCell();
			cell.setValue(scoreMap.get(address));
			cell.setPolygon(spatialIndexingService.polygonFromAddress(address));
			heatmap.add(cell);
		}
		return heatmap;
	}

	public List<MarkerVisualisation> generateMarkerVisualization(
			Float latitudeUpper,
			Float latitudeLower,
			Float longitudeLeft,
			Float longitudeRight,
			List<Long> categories
	) {
		List<Report> selectedReports = reportRepository.findBetweenBounds(longitudeLeft, longitudeRight, latitudeLower, latitudeUpper, categories);
		List<Cluster> clusters = weightedFixedRadiusNearestNeighbour(selectedReports);
		List<MarkerVisualisation> markerVisualisations = new ArrayList<>();
		for (Cluster cluster : clusters){
			MarkerVisualisation markerVisualisation = new MarkerVisualisation();
			markerVisualisation.setId(cluster.reportList().get(0).getId());
			markerVisualisation.setLatitude(cluster.center().getLatitude());
			markerVisualisation.setLongitude(cluster.center().getLongitude());
			markerVisualisation.setCategoryColor(cluster.category().getColor());
			markerVisualisation.setCategoryIcon(cluster.category().getIcon());
			if(cluster.score() >= cluster.category().getMinimumScore()){
				markerVisualisations.add(markerVisualisation);
			}
		}
		return markerVisualisations;
	}

	private List<Cluster> weightedFixedRadiusNearestNeighbour(List<Report> reports){
		Map<Report, List<Report>> neighbours = groupByDistanceAndCategory(reports);
		List<Map.Entry<Report, List<Report>>> entries = new ArrayList<>(neighbours.entrySet());

		// Sortiere die Liste basierend auf der Größe der Listen
		entries.sort((a, b) -> Integer.compare(a.getValue().size(), b.getValue().size()));

		List<Cluster> clusters = new ArrayList<>();
		while (!entries.isEmpty()){
			Map.Entry<Report, List<Report>> entry = entries.remove(0);
			Report report = entry.getKey();
			entry.getValue().add(report);
			LatLon meanPosition = calculateMeanPosition(entry.getValue());
			float score = clusterScore(entry.getValue(), meanPosition);
			Cluster cluster = new Cluster(meanPosition, score, report.getCategory(), entry.getValue());
			clusters.add(cluster);
			for (Report neighbour : entry.getValue()){
				entries.removeIf(e -> e.getKey().getId().equals(neighbour.getId()));
			}
		}
		return clusters;
	}

	private float clusterScore(List<Report> reports, LatLon clusterCenter){
		float score = 0.5f;
		Map<Long, Integer> userDamping = new HashMap<>();
		for (Report report : reports){
			int damping = userDamping.getOrDefault(report.getUser().getId(), 0);
			double distance = spatialIndexingService.distance(new LatLng(report.getLatitude(), report.getLongitude()), new LatLng(clusterCenter.getLatitude(), clusterCenter.getLongitude()), LengthUnit.m);
			score += 1f; // Math.pow(0.98f, distance)
			userDamping.put(report.getUser().getId(), userDamping.getOrDefault(report.getUser().getId(), 0) + 1);
		}
		return score;
	}

	private LatLon calculateMeanPosition(List<Report> reports){
		float latitude = 0;
		float longitude = 0;
		for (Report report : reports){
			latitude += report.getLatitude();
			longitude += report.getLongitude();
		}
		return new LatLon(latitude/reports.size(), longitude/reports.size());
	}

	private Map<Report, List<Report>>  groupByDistanceAndCategory(List<Report> reports){
		Map<Report, List<Report>> neighbours = new HashMap<>();
		for (Report report1 : reports){
			if(report1.getCategory().getAllowDiscrete()){
				neighbours.put(report1, new ArrayList<>());
			}
			for(Report report2 : reports){
				if(!Objects.equals(report1.getId(), report2.getId())
						&& report1.getCategory().getAllowDiscrete()
						&& Objects.equals(report1.getCategory().getId(), report2.getCategory().getId())
						&& spatialIndexingService.distance(new LatLng(report1.getLatitude(), report1.getLongitude()), new LatLng(report2.getLatitude(), report2.getLongitude()), LengthUnit.m) < report1.getCategory().getAggregationRadiusMeters()
				){
					neighbours.get(report1).add(report2);
				}
			}
		}
		return neighbours;
	}

}
