/**
 * This Module contains functions to communicate with the CityGuard API.
 * Whenever a different Module needs to communicate with the API, it should import this Module and call the functions defined here.
 * @module nominatim-api
 */

import {geoCoderApiEndpoint} from "../../config/config.js";
import {geoCoderApiEndpointReverse} from "../../config/config.js";


/**
 * Fetches the geolocation objects which matches the locationString.
 * @param locationString
 * @param func
 */
export function fetchCoordinatesMatchingString(locationString,func) {
	fetch(geoCoderApiEndpoint + locationString)
		.then(response => response.json())
		.then(data => {
			func(data)

		});
}

export function fetchNameFromCoordinates(lat,lon, func) {
	fetch(geoCoderApiEndpointReverse+"lat="+lat+"&lon="+lon)
		.then(response => response.json())
		.then(data => {
			func(data)
		});
}
