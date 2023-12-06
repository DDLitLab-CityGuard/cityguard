/**
 * This module is the entry point of the application.
 * It contains the main function that is called when the DOM is loaded.
 * @module application
 */
import {checkboxChanged, closeTheModal, fetchCategoriesAndRenderOptions, validationAndSubmit} from "./formservice.js";
import {fetchAndRenderReports} from "./mapdisplayservice.js";


/**
 * Main function of the application. It is called when the DOM is loaded.
 * Hear we start the application by adding event listeners to the buttons and initializing the map.
 */
function main() {
	let reportButton = document.getElementById('report_button');
	let submitButton = document.getElementById('submitevent');
	let closeButton = document.getElementById('closeModal');
	let checkbox = document.getElementById("locationcheckbox");
	let inputField = document.getElementById("location");
	let hiddenInputField = document.getElementById("location_hidden");
	let submitForm = document.getElementById('submit_form');
	let map = L.map('map').setView([53.566819239846915, 10.004717089957754], 13);

	reportButton.addEventListener('click', fetchCategoriesAndRenderOptions);
	submitButton.addEventListener('click', (e) => validationAndSubmit(submitForm, e, closeButton));
	checkbox.addEventListener("change", () => checkboxChanged(checkbox, inputField, hiddenInputField));

	L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
		maxZoom: 19,
		attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
	}).addTo(map);
	let markergroup = L.layerGroup().addTo(map);
	let heatmapgroup = L.layerGroup().addTo(map);
	map.on('moveend', () => fetchAndRenderReports(map, heatmapgroup, markergroup));
}

document.addEventListener('DOMContentLoaded', main);
