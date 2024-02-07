/**
 * This module provides functions for navigation related tasks.
 *
 * @module navigationservice
 */


export function startNavigation() {
	if ("geolocation" in navigator) {
		navigator.geolocation.watchPosition(function(position) {
			const latitude = position.coords.latitude;
			const longitude = position.coords.longitude;
			localStorage.setItem("lastCoordinates", JSON.stringify({latitude: latitude, longitude: longitude}));
			document.dispatchEvent(new CustomEvent("userLocationChanged", {detail: {latitude: latitude, longitude: longitude}}));
		});
	} else {
		console.log("Geolocation wird in diesem Browser nicht unterstützt.");
	}
}

export function renderUserLocation(e, map, userLocationLayer) {
	const latitude = e.detail.latitude;
	const longitude = e.detail.longitude;
	if (latitude && longitude) {

		userLocationLayer.clearLayers();
		const options = {
			color: '#a2aeff',
			radius: 5,
			weight: 2,
			fillColor: 'blue',
			fillOpacity: 1
		}
		L.circle([latitude, longitude], {radius: 50, weight: 0}).addTo(userLocationLayer);
		L.circleMarker([latitude, longitude], options).addTo(userLocationLayer);
	}
}
