var map = L.map('map').setView([53.566819239846915, 10.004717089957754], 13);


L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);


map.on('moveend', function(ev) {
    fetchandrender();
});

//fetch data from api
function fetchandrender() {
    fetch('http://127.0.0.1:5123/api/fetchreports')
        .then(response => response.json())
        .then(data => {
            console.log(data);
        });
}