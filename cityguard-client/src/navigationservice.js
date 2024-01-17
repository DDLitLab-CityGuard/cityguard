/**
 * This module provides functions for navigation related tasks.
 *
 * @module navigationservice
 */

let lastCoordinates = null;

/**
 * This function returns the current coordinates of the user in latitude and longitude.
 * When the user did not agree to share his location yet, he will be asked to do so.
 * If the browser does not support geolocation, or if the website is not on https this will not work.
 *
 * @returns {null|Promise<unknown>}
 */
export function getCoordinates() {
	if (lastCoordinates) {
		return new Promise((resolve) => {
			resolve(lastCoordinates);
		});
	}
	if (navigator.geolocation) {
		return new Promise((resolve) => {
			navigator.geolocation.getCurrentPosition(function(position) {
				const latitude = position.coords.latitude;
				const longitude = position.coords.longitude;
				document.dispatchEvent(new CustomEvent("NavigationUpdate", { detail: {latitude, longitude} }));
				resolve({ latitude, longitude });
			});
		});
	} else {
		alert("Geolokalisierung wird in diesem Browser nicht unterstützt.");
		return null;
	}

}

/**
 * This function triggers a navigation update.
 * It will fetch the current coordinates and dispatch a NavigationUpdate event with the coordinates as detail.
 */
export function triggerNavigationUpdate() {
	getCoordinates().then((coordinates) => {
		lastCoordinates = coordinates;
	});
}

export function renderUserLocation(e, map, userLocationLayer) {
	const latitude = e.detail.latitude;
	const longitude = e.detail.longitude;
	if (latitude && longitude) {
		console.log("renderUserLocation", latitude, longitude);

		userLocationLayer.clearLayers();
		const options = {
			color: 'white',
			radius: 6,
			weight: 2,
			fillColor: 'blue',
			fillOpacity: 1
		}
		const rim = L.circle([latitude, longitude], {radius: 100, weight: 0}).addTo(userLocationLayer);
		const circle_marker = L.circleMarker([latitude, longitude], options).addTo(userLocationLayer);
	}
}
