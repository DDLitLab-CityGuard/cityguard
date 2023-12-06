/**
 * This module provides functions for navigation related tasks.
 *
 * @module navigationservice
 */

/**
 * This function returns the current coordinates of the user in latitude and longitude.
 * When the user did not agree to share his location yet, he will be asked to do so.
 * If the browser does not support geolocation, or if the website is not on https this will not work.
 *
 * @returns {null|Promise<unknown>}
 */
export function getCoordinates() {
	if (navigator.geolocation) {
		return new Promise((resolve) => {
			navigator.geolocation.getCurrentPosition(function(position) {
				const latitude = position.coords.latitude;
				const longitude = position.coords.longitude;
				resolve({ latitude, longitude });
			});
		});
	} else {
		alert("Geolokalisierung wird in diesem Browser nicht unterstützt.");
		return null;
	}
}
