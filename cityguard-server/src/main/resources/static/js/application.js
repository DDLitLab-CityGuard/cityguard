/**
 * This module is the entry point of the application.
 * It contains the main function that is called when the DOM is loaded.
 * @module application
 */


import './smoothzoom.js';

import {checkboxChanged, clearForm, fetchCategoriesAndRenderOptions, validationAndSubmit, dateCheckboxChanged} from "./formservice.js";
import {fetchAndRenderReports} from "./mapdisplayservice.js";
import {fetchClickCoordinatesAndOpenForm, fetchCoordinatesFromInput} from "./geocodingservice.js";
import {displayMapFilterButton} from "./mapfilterservice.js";
import {fetchCategories} from "./apiwrapper/cityguard-api.js";
import {renderUserLocation, startNavigation} from "./navigationservice.js";


/**
 * Main function of the application. It is called when the DOM is loaded.
 * Hear we start the application by adding event listeners to the buttons and initializing the map.
 */
function main() {

	startNavigation();



	const dateTimeCheckbox = document.getElementById("datetimecheckbox");
	let reportButton = document.getElementById('report_button');
	let submitButton = document.getElementById('submitevent');
	let closeButton = document.getElementById('closeModal');
	let checkbox = document.getElementById("locationcheckbox");
	let inputField = document.getElementById("location");
	let hiddenInputField = document.getElementById("location_hidden");
	let submitForm = document.getElementById('submit_form');
	let locationInput = document.getElementById('location');
	let map = L.map('map', {
		scrollWheelZoom: false, // disable original zoom function
		smoothWheelZoom: true,  // enable smooth zoom
		smoothSensitivity: 3,
		maxBounds: [ [-40, -80], [80, 80] ],
		minZoom: 5,
	})
	if(localStorage.getItem("lastCoordinates")){
		let lastCoordinates = JSON.parse(localStorage.getItem("lastCoordinates"));
		map.setView([lastCoordinates.latitude, lastCoordinates.longitude], 18);
	}else {
		map.setView([53.550, 10.00], 13)
	}

	map.on('click', function(e) {clearForm(submitForm,inputField);fetchClickCoordinatesAndOpenForm(e,locationInput,hiddenInputField)});
	reportButton.addEventListener('click', (e) =>{fetchCategoriesAndRenderOptions();clearForm(submitForm,inputField);e.stopPropagation();});
	dateTimeCheckbox.addEventListener('change', () => dateCheckboxChanged());
	submitButton.addEventListener('click', (e) => validationAndSubmit(submitForm, e, closeButton));
	checkbox.addEventListener("change", () => checkboxChanged(checkbox, inputField, hiddenInputField));
	locationInput.addEventListener('keyup', (e) => fetchCoordinatesFromInput(locationInput,e));
	L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
		maxZoom: 19,
		attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
	}).addTo(map);
	let markergroup = L.layerGroup().addTo(map);
	let heatmapgroup = L.layerGroup().addTo(map);
	let userLocationLayer = L.layerGroup().addTo(map);
	map.on('movestart', () => fetchAndRenderReports(map, heatmapgroup, markergroup));
	map.on('moveend', () => fetchAndRenderReports(map, heatmapgroup, markergroup));
	document.addEventListener('ReportSubmitted', () => setTimeout(() => fetchAndRenderReports(map, heatmapgroup, markergroup), 200));
	document.addEventListener('NavigationUpdate', (e) => renderUserLocation(e, map, userLocationLayer));
	if(localStorage.getItem("lastCoordinates")){
		let lastCoordinates = JSON.parse(localStorage.getItem("lastCoordinates"));
		renderUserLocation({detail: lastCoordinates}, map, userLocationLayer)
	}

	fetchCategories((data) => {
		displayMapFilterButton(data, map, heatmapgroup, markergroup);
		fetchAndRenderReports(map, heatmapgroup, markergroup);
	});


}

document.addEventListener('DOMContentLoaded', main);
