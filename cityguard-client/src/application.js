/**
 * This module is the entry point of the application.
 * It contains the main function that is called when the DOM is loaded.
 * @module application
 */

//css
import 'bootstrap/dist/css/bootstrap.min.css'
import 'leaflet/dist/leaflet.css';
import './main.css'

//js
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import '@popperjs/core/dist/umd/popper.min.js'
import 'bootstrap/dist/js/bootstrap.min.js'
import L from 'leaflet';


import {checkboxChanged, clearForm, fetchCategoriesAndRenderOptions, validationAndSubmit} from "./formservice.js";
import {fetchAndRenderReports} from "./mapdisplayservice.js";
import {fetchClickCoordinatesAndOpenForm, fetchCoordinatesFromInput} from "./geocodingservice.js";
import {displayMapFilterButton} from "./mapfilterservice.js";
import { icon, Marker } from 'leaflet';
import {fetchCategories} from "./apiwrapper/cityguard-api.js";
import {getCoordinates} from "./navigationservice.js";


/**
 * Main function of the application. It is called when the DOM is loaded.
 * Hear we start the application by adding event listeners to the buttons and initializing the map.
 */
function main() {



	const iconRetinaUrl = 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png';
	const iconUrl = 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png';
	const shadowUrl = 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png';
	Marker.prototype.options.icon = icon({
		iconRetinaUrl,
		iconUrl,
		shadowUrl,
		iconSize: [25, 41],
		iconAnchor: [12, 41],
		popupAnchor: [1, -34],
		tooltipAnchor: [16, -28],
		shadowSize: [41, 41]
	});



	let reportButton = document.getElementById('report_button');
	let submitButton = document.getElementById('submitevent');
	let closeButton = document.getElementById('closeModal');
	let checkbox = document.getElementById("locationcheckbox");
	let inputField = document.getElementById("location");
	let hiddenInputField = document.getElementById("location_hidden");
	let submitForm = document.getElementById('submit_form');
	let locationInput = document.getElementById('location');
	let map = L.map('map', {
		maxBounds: [ [-40, -80], [80, 80] ],
		minZoom: 5,
	}).setView([53.550, 10.00], 13)

	map.on('click', function(e) {clearForm(submitForm,inputField);fetchClickCoordinatesAndOpenForm(e,locationInput,hiddenInputField)});
	reportButton.addEventListener('click', (e) =>{fetchCategoriesAndRenderOptions();clearForm(submitForm,inputField);e.stopPropagation();});

	submitButton.addEventListener('click', (e) => validationAndSubmit(submitForm, e, closeButton));
	checkbox.addEventListener("change", () => checkboxChanged(checkbox, inputField, hiddenInputField));
	locationInput.addEventListener('keyup', (e) => fetchCoordinatesFromInput(locationInput,e));
	L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
		maxZoom: 19,
		attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
	}).addTo(map);
	let markergroup = L.layerGroup().addTo(map);
	let heatmapgroup = L.layerGroup().addTo(map);
	map.on('movestart', () => fetchAndRenderReports(map, heatmapgroup, markergroup));
	map.on('moveend', () => fetchAndRenderReports(map, heatmapgroup, markergroup));

	fetchCategories((data) => {
		displayMapFilterButton(data, map, heatmapgroup, markergroup);
		fetchAndRenderReports(map, heatmapgroup, markergroup);
	});


}

document.addEventListener('DOMContentLoaded', main);
