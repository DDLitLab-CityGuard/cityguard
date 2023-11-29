export class Mapdisplayservice{

    constructor(map) {

        this.map=map
        L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
            maxZoom: 19,
            attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
        }).addTo(map);
        this.markergroup = L.layerGroup().addto(map);
        this.heatmapgroup = L.layerGroup().addto(map);

        map.on('moveend', this.fetchandrender);


    }
    fetchandrender(){
        let latl = this.map.getBounds().getSouthWest().lat - 0.01
        let lonl = this.map.getBounds().getSouthWest().lng -0.01
        let latu = this.map.getBounds().getNorthEast().lat + 0.01
        let  lonu = this.map.getBounds().getNorthEast().lng + 0.01
        fetch(document.location.protocol+"//"+document.location.hostname+`:5123/api/fetch_reports?latitudeLower=${latl}&latitudeUpper=${latu}&longitudeLeft=${lonl}&longitudeRight=${lonu}`)
            .then(response => response.json())
            .then(data => {
                this.heatmapgroup.clearLayers();
                this.markergroup.clearLayers();
                for (var i = 0; i < data.markers.length; i++) {
                    L.marker([data.markers[i].latitude, data.markers[i].longitude]).addTo(this.markergroup);
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
                    L.polygon(latlngs, {color: '#ff0000', weight: 0, fillOpacity: data.heatmap[i].value}).addTo(this.heatmapgroup);
                }
            });
    }
}