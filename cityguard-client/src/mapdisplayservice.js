/**
 * This module is responsible for the display of the map together with the collected heatmap and markers from the api.
 * It delegates the fetching of the data to the api module and renders the data on the map.
 *
 * @module mapdisplayservice
 */
import {fetchReports} from "./apiwrapper/cityguard-api.js";

/**
 * This function collects the data from the api and renders it on the map by putting it into the heatmapGroup and markerGroup.
 *
 * @param map
 * @param heatmapGroup
 * @param markerGroup
 */
export function fetchAndRenderReports(map, heatmapGroup, markerGroup){

	fetchReports(
		map.getBounds().getSouthWest().lat - 0.01,
		map.getBounds().getNorthEast().lat + 0.01,
		map.getBounds().getSouthWest().lng -0.01,
		map.getBounds().getNorthEast().lng + 0.01,
		(data) => {
			heatmapGroup.clearLayers();
			markerGroup.clearLayers();
			for (let i = 0; i < data.markers.length; i++) {
				let category_id = data.markers[i].category.id;
				let filter = document.getElementById("category-filter-" + category_id).checked;
				if (!filter) {
					continue;
				}
				L.marker([data.markers[i].latitude, data.markers[i].longitude]).addTo(markerGroup);
			}
			for(let i = 0; i < data.heatmap.length; i++){
				const polygon = data.heatmap[i].polygon;
				const value = data.heatmap[i].value;
				const latLongs = polygon.map((point) => [point.latitude, point.longitude])
				L.polygon(latLongs, {color: 'black', weight: 0.1, fillOpacity: value, fillColor: 'red'}).addTo(heatmapGroup);
			}
		}
	)
}
