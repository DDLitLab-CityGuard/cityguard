import {fetchReports} from "./api/cityguard-api.js";

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
                L.marker([data.markers[i].latitude, data.markers[i].longitude]).addTo(markerGroup);
            }
            for(let i = 0; i < data.heatmap.length; i++){
                const x = data.heatmap[i].latitude;
                const y = data.heatmap[i].longitude;
                const sizeLat = data.heatmap[i].sizeLat;
                const sizeLon = data.heatmap[i].sizeLon;
                const latLongs = [
                    [x, y],
                    [x + (sizeLat), y],
                    [x + (sizeLat), y + sizeLon],
                    [x, y + sizeLon],
                ];
                L.polygon(latLongs, {color: '#ff0000', weight: 0, fillOpacity: data.heatmap[i].value}).addTo(heatmapGroup);
            }
        }
    )
}