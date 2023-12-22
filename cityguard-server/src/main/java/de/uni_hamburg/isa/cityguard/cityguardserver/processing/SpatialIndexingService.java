package de.uni_hamburg.isa.cityguard.cityguardserver.processing;


import com.uber.h3core.H3Core;
import com.uber.h3core.util.LatLng;
import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.HeatmapCell;
import de.uni_hamburg.isa.cityguard.cityguardserver.api.dto.LatLon;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.Report;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class SpatialIndexingService {

    private final H3Core h3 = H3Core.newInstance();

    public SpatialIndexingService() throws IOException {
    }

    public List<HeatmapCell> calculateHeatmap(List<Report> reports, int resolution) {
        HashMap<String, Integer> address_map = new HashMap<>();
        for (Report report : reports) {
            LatLon coordinate = new LatLon(report.getLatitude(), report.getLongitude());
            String hexAddr = h3.latLngToCellAddress(coordinate.getLatitude(), coordinate.getLongitude(), resolution);
            h3.gridDisk(hexAddr, 1).forEach(hexAddr2 -> {
                if (address_map.containsKey(hexAddr2)) {
                    address_map.put(hexAddr2, address_map.get(hexAddr2) + 1);
                } else {
                    address_map.put(hexAddr2, 1);
                }
            });
            if (address_map.containsKey(hexAddr)) {
                address_map.put(hexAddr, address_map.get(hexAddr) + 1);
            } else {
                address_map.put(hexAddr, 1);
            }
        }
        List<HeatmapCell> heatmap = new ArrayList<>();
        for (String hexAddr : address_map.keySet()) {
            HeatmapCell cell = new HeatmapCell();
            cell.setValue(address_map.get(hexAddr)/10.0f);
            cell.setPolygon(h3.cellToBoundary(hexAddr).stream().map(latLng -> new LatLon((float) latLng.lat, (float) latLng.lng)).toList());
            heatmap.add(cell);
        }
        return heatmap;
    }

    public List<HeatmapCell> calculateAllCells(int resolution, LatLon... coordinates) {
        List<HeatmapCell> heatmap = new ArrayList<>();
        List<LatLng> latLngs = Arrays.stream(coordinates).map(coordinate -> new LatLng(coordinate.getLatitude(), coordinate.getLongitude())).toList();
        List<String> addresses = h3.polygonToCellAddresses(latLngs,null, resolution);
        for (String hexAddr : addresses) {
            HeatmapCell cell = new HeatmapCell();
            cell.setValue(0.1f);
            cell.setPolygon(h3.cellToBoundary(hexAddr).stream().map(latLng -> new LatLon((float) latLng.lat, (float) latLng.lng)).toList());
            heatmap.add(cell);
        }
        return heatmap;
    }
}
