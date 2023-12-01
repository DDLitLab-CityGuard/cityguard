
const apiEndpoint = `${document.location.protocol}//${document.location.hostname}:5123/api`

export function fetchCategories(func){
    fetch(`${apiEndpoint}/fetch_categories`)
        .then(response => response.json())
        .then(data => {
            func(data)
        });
}

export function fetchReports(latitudeLower, latitudeUpper, longitudeLeft, longitudeRight, func){
    fetch(`${apiEndpoint}/fetch_reports` + new URLSearchParams(

    ))
        .then(response => response.json())
        .then(data => {

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
            !response.ok ? throw new Error(`HTTP error! Status: ${response.status}`) : null;
        });
}

