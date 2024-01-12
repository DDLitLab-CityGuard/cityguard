package de.uni_hamburg.isa.cityguard.cityguardserver.processing;


import com.uber.h3core.H3Core;
import com.uber.h3core.LengthUnit;
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

/**
 * Service for spatial indexing.
 * It provides everything related to the H3 library.
 */
@Service
public class SpatialIndexingService {

	private final H3Core h3 = H3Core.newInstance();

	public SpatialIndexingService() throws IOException {
	}

	/**
	 * This method calculates a heatmap based on a list of reports.
	 * @param reports The reports to calculate the heatmap from
	 * @param resolution The resolution of the heatmap
	 * @return A list of heatmap cells
	 */
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

	/**
	 * This method calculates a List of heatmap cells between four coordinates.
	 * Every cell is calculated with the given resolution and the value is set to 0.1.
	 * @return A list of heatmap cells
	 */
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

	/**
	 * This method calculates an appropriate resolution for a heatmap based on two coordinates that should be the upper left and lower right corner of the screen.
	 * @param a The first coordinate
	 * @param b The second coordinate
	 * @return The appropriate resolution for a heatmap of this size
	 */
	public int resolutionFromZoom(LatLng a, LatLng b) {
		double distance = Math.min(h3.greatCircleDistance(a, b, LengthUnit.km), 14000);
		int resolution = 10;
		resolution = distance > 9 ? 9 : resolution;
		resolution = distance > 20 ? 8 : resolution;
		resolution = distance > 60 ? 7 : resolution;
		resolution = distance > 120 ? 6 : resolution;
		resolution = distance > 240 ? 5 : resolution;
		resolution = distance > 480 ? 4 : resolution;
		resolution = distance > 1800 ? 3 : resolution;
		resolution = distance > 6000 ? 2 : resolution;
		resolution = distance > 12000 ? 1 : resolution;
		return resolution;
	}
}
