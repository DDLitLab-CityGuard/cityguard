/**
 * This Module contains functions to communicate with the CityGuard API.
 * Whenever a different Module needs to communicate with the API, it should import this Module and call the functions defined here.
 * @module cityguard-api
 */

/**
 * The Base URL of the CityGuard API.
 * @type {string}
 */
const apiEndpoint = `${document.location.protocol}//${document.location.hostname}:8088/api`

/**
 * Fetches information about the existing Categories from the API.
 * @param func
 */
export function fetchCategories(func){
	fetch(`${apiEndpoint}/fetch_categories`)
		.then(response => response.json())
		.then(data => {
			func(data)
		});
}
export function fetchCoordinatesMatchingString(locationString){

}

/**
 * Fetches the coordinates of all Reports within the specified area from the API.
 * The response also returns a heatmap for the specified area that represents the density of Reports within the area.
 *
 * @param latitudeLower
 * @param latitudeUpper
 * @param longitudeLeft
 * @param longitudeRight
 * @param func
 */
export function fetchReports(latitudeLower, latitudeUpper, longitudeLeft, longitudeRight, func){
	fetch(`${apiEndpoint}/fetch_reports?` + new URLSearchParams(
		{
			latitudeLower: latitudeLower,
			latitudeUpper: latitudeUpper,
			longitudeLeft: longitudeLeft,
			longitudeRight: longitudeRight
		}
	))
		.then(response => response.json())
		.then(data => {
			func(data)
		});
}

/**
 * Submits a new Report to the API.
 * @param report
 */
export function submitReport(report){
	const options = {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: report
	};

	fetch(`${apiEndpoint}/submit_report`, options)
		.then(response => {
			if (!response.ok) {
				throw new Error(`HTTP error! Status: ${response.status}`);
			}
		});

}

