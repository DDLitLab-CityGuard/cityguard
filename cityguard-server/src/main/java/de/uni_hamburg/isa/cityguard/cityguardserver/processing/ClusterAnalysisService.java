package de.uni_hamburg.isa.cityguard.cityguardserver.processing;

import com.uber.h3core.LengthUnit;
import com.uber.h3core.util.LatLng;
import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.HeatmapCell;
import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.LatLon;
import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.MarkerVisualisation;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.ReportRepository;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ClusterAnalysisService {

    private final ReportRepository reportRepository;
    private final SpatialIndexingService spatialIndexingService;

    public List<HeatmapCell> generateHeatmapVisualization(
            Float latitudeUpper,
            Float latitudeLower,
            Float longitudeLeft,
            Float longitudeRight,
            Long heatmapCategory
    ){
        List<Report> heatmapReports = reportRepository.findBetweenBounds(longitudeLeft, longitudeRight, latitudeLower, latitudeUpper, List.of(heatmapCategory));
        int resolution = spatialIndexingService.resolutionFromZoom(new LatLng(latitudeUpper, longitudeLeft), new LatLng(latitudeLower, longitudeRight));

        List<Cluster> clusters = weightedFixedRadiusNearestNeighbour(heatmapReports);
        Map<String, Float> scoreMap = new HashMap<>();
        for (Cluster cluster : clusters){
            if (cluster.score() >= cluster.category().getMinimumScore()){
                Map<String, List<Report>> addressMap = spatialIndexingService.groupByCell(cluster.reportList(), resolution);
                for (String address : addressMap.keySet()){
                    scoreMap.put(address, 0.4f);
                }
                scoreMap.put(spatialIndexingService.clusterAddress(cluster, resolution), 0.6f);
            }
        }

        List<String> addressList = spatialIndexingService.addressListFromBounds(latitudeUpper, latitudeLower, longitudeLeft, longitudeRight, resolution);

        List<HeatmapCell> heatmap = new ArrayList<>();
        for (String address : addressList){
            float score = scoreMap.getOrDefault(address, 0.1f);
            HeatmapCell cell = new HeatmapCell();
            cell.setValue(Math.min(score, 0.6f));
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
            score += (float) (1f * Math.pow(0.98f, distance) * (1f / (1f + damping)));
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
