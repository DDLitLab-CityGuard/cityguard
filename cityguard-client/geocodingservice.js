
import {fetchCoordinatesMatchingString} from "./apiwrapper/nominatim-api.js";
import {clearTheValidations} from "./formservice.js";
import {fetchNameFromCoordinates} from "./apiwrapper/nominatim-api.js";


/**
 *Calls the nominatim api to fetch coordinates matching the input string and displays them with the jquery autocomplete widget
 *On selection of an autocomplete suggestion, the hidden input field is set to the coordinates of the selected suggestion
 * @param inputField
 * @param e
 */
export function fetchCoordinatesFromInput(inputField, e) {
	if (e.key === 'Enter' || e.keyCode === 13) {
		// Do nothing or return early to skip execution
		return;
	}
	let hiddenInputField = document.getElementById("location_hidden");
	hiddenInputField.value = "";
	let locationString = inputField.value
	fetchCoordinatesMatchingString(locationString, function (data) {
		clearTheValidations();//Validation part
		let formateddata= formatData(data);
		if (formateddata.length>0){
			hiddenInputField.value = formatData(data)[0].value;		}
		else{//validation part
			showValidationError();
		}
		});

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

export function fetchClickCoordinatesAndOpenForm(e,locationInput,hiddenInputField){
	hiddenInputField.value = e.latlng.lat+","+e.latlng.lng;
	fetchNameFromCoordinates(e.latlng.lat, e.latlng.lng, (data) => {
		locationInput.value = data.display_name;
	});
	$('#form_modal').modal('show');

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



function showValidationError() {
	document.getElementById("location").classList.add("is-invalid")
	document.getElementById("locationcheckbox").classList.add("is-invalid")
	let error =document.getElementById("location-feedback")
	let text = document.createTextNode("No mathcing coordinates found");
	error.appendChild(text);
}
