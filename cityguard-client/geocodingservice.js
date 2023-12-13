
import {fetchCoordinatesMatchingString} from "./apiwrapper/nominatim-api.js";


/**
 *Calls the nominatim api to fetch coordinates matching the input string and displays them with the jquery autocomplete widget
 *On selection of an autocomplete suggestion, the hidden input field is set to the coordinates of the selected suggestion
 * @param inputField
 * @param e
 */
export function fetchCoordinatesFromInput(inputField, e) {
	let hiddenInputField = document.getElementById("location_hidden");
	hiddenInputField.value = "";
	let locationString = inputField.value
	$(inputField).autocomplete({
			delay: 400,
			minLength: 2,
			source: function (request, response) {
				fetchCoordinatesMatchingString(locationString, function (data) {
					response(formatData(data));
				});
			}
			,select: function (event, ui) {
				event.preventDefault();
			},appendTo: "#submit_form"
		});
	$(inputField).on( "autocompleteselect", function( event, ui ) {
		$(event.target.value=ui.item.label);
		$(hiddenInputField).val(ui.item.value);
	} );
}

function formatData(data) {
	let result = [];
	for (let i = 0; i < data.length; i++) {
		result.push({
			label: data[i].display_name,
			value: data[i].lat+","+data[i].lon
		});
	}
	return result;
}
