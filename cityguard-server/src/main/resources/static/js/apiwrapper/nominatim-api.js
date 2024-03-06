/**
 * This Module contains functions to communicate with the CityGuard API.
 * Whenever a different Module needs to communicate with the API, it should import this Module and call the functions defined here.
 * @module nominatim-api
 */

import {geoCoderApiEndpoint} from "../config.js";
import {geoCoderApiEndpointReverse} from "../config.js";


/**
 * Fetches the geolocation objects which matches the locationString.
 * @param locationString
 * @param func
 */
export function fetchCoordinatesMatchingString(locationString,func) {
	fetch(geoCoderApiEndpoint + locationString)
		.then(response => {
			if (response.status === 401) {
				// Redirect to the desired page
				window.location.href = '/login';
			}
			return response.json(); // or handle other status codes as needed
		})
		.then(data => {
			func(data)

		});
}

export function fetchNameFromCoordinates(lat,lon, func) {
	fetch(geoCoderApiEndpointReverse+"lat="+lat+"&lon="+lon)
		.then(response => {
			if (response.status === 401) {
				// Redirect to the desired page
				window.location.href = '/login';
			}
			return response.json(); // or handle other status codes as needed
		})
		.then(data => {
			func(data)
		});
}
