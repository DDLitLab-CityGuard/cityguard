var map = L.map('map').setView([53.566819239846915, 10.004717089957754], 13);


L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);


map.on('moveend', function(ev) {
    fetchandrender();
});


const heatmapGroup = L.layerGroup().addTo(map);
const markerGroup = L.layerGroup().addTo(map);



function fetchandrender() {
    latl = map.getBounds().getSouthWest().lat - 0.01
    lonl = map.getBounds().getSouthWest().lng -0.01
    latu = map.getBounds().getNorthEast().lat + 0.01
    lonu = map.getBounds().getNorthEast().lng + 0.01
    fetch(`http://127.0.0.1:5123/api/fetchreports?latitudeLower=${latl}&latitudeUpper=${latu}&longitudeLeft=${lonl}&longitudeRight=${lonu}`)
        .then(response => response.json())
        .then(data => {
            heatmapGroup.clearLayers();
            markerGroup.clearLayers();
            for (var i = 0; i < data.markers.length; i++) {
                L.marker([data.markers[i].latitude, data.markers[i].longitude]).addTo(markerGroup);
            }
            for(var i = 0; i < data.heatmap.length; i++){
                const x = data.heatmap[i].latitude;
                const y = data.heatmap[i].longitude;
                const sizeLat = data.heatmap[i].sizeLat;
                const sizeLon = data.heatmap[i].sizeLon;
                var latlngs = [
                    [x, y],
                    [x + (sizeLat), y],
                    [x+ (sizeLat), y + sizeLon],
                    [x, y + sizeLon],
                ];
                L.polygon(latlngs, {color: '#ff0000', weight: 0, fillOpacity: data.heatmap[i].value}).addTo(heatmapGroup);
            }
        });
}