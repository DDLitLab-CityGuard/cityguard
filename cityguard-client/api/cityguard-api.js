
const apiEndpoint = `${document.location.protocol}//${document.location.hostname}:8088/api`

export function fetchCategories(func){
	fetch(`${apiEndpoint}/fetch_categories`)
		.then(response => response.json())
		.then(data => {
			func(data)
		});
}
export function fetchCoordinatesMatchingString(locationString){

}

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

