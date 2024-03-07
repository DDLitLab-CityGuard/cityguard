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
        List<HeatmapCell> heatmap = spatialIndexingService.calculateHeatmap(heatmapReports, resolution);
        List<HeatmapCell> heatmap2 = spatialIndexingService.calculateAllCells(
                resolution,
                new LatLon(latitudeUpper, longitudeLeft),
                new LatLon(latitudeUpper, longitudeRight),
                new LatLon(latitudeLower, longitudeRight),
                new LatLon(latitudeLower, longitudeLeft)
        );
        heatmap.addAll(heatmap2);
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
        return weightedFixedRadiusNearestNeighbour(selectedReports);
    }

    private List<MarkerVisualisation> weightedFixedRadiusNearestNeighbour(List<Report> selectedReports) {
        Map<Report, List<Report>> neighbours = new HashMap<>();
        for (Report report1 : selectedReports){
            if(report1.getCategory().getAllowDiscrete()){
                neighbours.put(report1, new ArrayList<>());
            }
            for(Report report2 : selectedReports){
                if(!Objects.equals(report1.getId(), report2.getId())
                        && report1.getCategory().getAllowDiscrete()
                        && Objects.equals(report1.getCategory().getId(), report2.getCategory().getId())
                        && spatialIndexingService.distance(new LatLng(report1.getLatitude(), report1.getLongitude()), new LatLng(report2.getLatitude(), report2.getLongitude()), LengthUnit.m) < report1.getCategory().getAggregationRadiusMeters()
                ){
                    neighbours.get(report1).add(report2);
                }
            }
        }

        List<Map.Entry<Report, List<Report>>> entries = new ArrayList<>(neighbours.entrySet());

        // Sortiere die Liste basierend auf der Größe der Listen
        entries.sort((a, b) -> Integer.compare(a.getValue().size(), b.getValue().size()));

        List<MarkerVisualisation> markerReports = new ArrayList<>();

        while (!entries.isEmpty()){
            MarkerVisualisation markerVisualisation = new MarkerVisualisation();
            Map.Entry<Report, List<Report>> entry = entries.remove(0);
            Report report = entry.getKey();
            markerVisualisation.setId(report.getId());
            markerVisualisation.setLatitude(report.getLatitude());
            markerVisualisation.setLongitude(report.getLongitude());
            markerVisualisation.setCategoryColor(report.getCategory().getColor());
            markerVisualisation.setCategoryIcon(report.getCategory().getIcon());

            if(entry.getValue().size() + 1 >= report.getCategory().getMinimumReports()){
                markerReports.add(markerVisualisation);
            }

            for (Report neighbour : entry.getValue()){
                entries.removeIf(e -> e.getKey().getId().equals(neighbour.getId()));
            }


        }
        return markerReports;
    }
}
