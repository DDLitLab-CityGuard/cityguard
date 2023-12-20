/**
 *  This module contains all the functions that are needed to display, validate and submit the report form.
 *  When the user clicks on the "Report" button, the form is displayed in a modal. The user can then fill out the form and submit it.
 *  All those interactions are handled by the functions in this file.
 *  @module formservice
 */
import {submitReport, fetchCategories} from "./apiwrapper/cityguard-api.js";
import {getCoordinates} from "./navigationservice.js";
import {fetchNameFromCoordinates} from "./apiwrapper/nominatim-api.js";

/**
 * This function is called when the user clicks on the "Report" button.
 * It is responsible for fetching the categories from the API and rendering them as options in the dropdown menu for the category selection.
 */
export function fetchCategoriesAndRenderOptions() {
	let select = document.getElementById('selectCategory');
	removeCategoryOptions(select);
	fetchCategories((data) => {
		for (let i = 0; i<data.length; i++){
			let opt = document.createElement('option');
			opt.value = data[i].id;
			opt.innerHTML = data[i].name;
			select.appendChild(opt);}
	});

}

/**
 * This function is called when the user clicks on the "Submit" button.
 * It is responsible for validating the form and submitting it to the API by calling helper functions.
 *
 * @param submitForm
 * @param event
 * @param closeButton
 */
export function validationAndSubmit(submitForm, event, closeButton){

	event.preventDefault();
	clearTheValidations();

	if (!formIsInvalid(submitForm)) {
		//make fetch post request
		makePostRequest(submitForm);
		closeTheModal(closeButton);
		document.getElementById("location").disabled = false;
		document.getElementById("location").classList.remove("text-muted");
		//clear the model form fields
		submitForm.reset()
	}
}

/**
 * This function is called when the user clicks on the checkbox for the current location.
 * It is responsible for disabling the input field for the location and filling it with the current location.
 *
 * @param checkbox
 * @param inputField
 * @param hiddenInputField
 * @returns {Promise<void>}
 */
export async function checkboxChanged(checkbox, inputField, hiddenInputField){
	if (checkbox.checked) {
		inputField.disabled = true;
		inputField.classList.add("text-muted");
		const coordinates = await getCoordinates();
		fetchNameFromCoordinates(coordinates.latitude,coordinates.longitude, function (data) {
			inputField.value = data['display_name'];
		});
		hiddenInputField.value = `${coordinates.latitude},${coordinates.longitude}`;
	} else {
		inputField.disabled = false;
		inputField.classList.remove("text-muted");
	}
}

/**
 * This function provides a simple way to close the modal programmatically.
 * @param closeButton
 */
export function closeTheModal(closeButton){
	closeButton.click()
}

function removeCategoryOptions(select){
	// Loop through all the options in reverse order
	for (let i = select.options.length - 1; i >= 0; i--) {
		// Remove the option at index i
		select.remove(i);
	}
}

function formIsInvalid(form){
	let json= getTheFormData(form)
	json=JSON.parse(json)
	console.log(json)
	let locaval=validateLocation(json.location_hidden,json.currentLocation)
	let datetimeval=validateDateTime(json.date,json.time,json.currentDateTime)
	let descval=validateDesc(json.desc)

	return !(descval == true && datetimeval == true && locaval == true);

}

function validateLocation(location,checkBox){
	if (checkBox == undefined){
		let regex =/[0-9]*\.[0-9]+,[0-9]*\.[0-9]+/i;
		if (location==""){
			document.getElementById("location").classList.add("is-invalid")
			document.getElementById("locationcheckbox").classList.add("is-invalid")
			let error =document.getElementById("location-feedback")
			var text = document.createTextNode("Please check the checkbox or type the coordinates");
			error.appendChild(text);
			return false;
		} else {
			if(location.match(regex)==null){
				document.getElementById("location").classList.add("is-invalid")
				document.getElementById("locationcheckbox").classList.add("is-invalid")
				let error=document.getElementById("location-feedback")
				let text = document.createTextNode("Please correct the coordinates");
				error.appendChild(text);
				return false;}

			return true;
		}
	} else {
		return true;
	}
}

function validateDateTime(date,time,checkBox){
	if (checkBox == undefined){
		if (date=="" && time==""){//TODO regex
			document.getElementById("datetimegroup").classList.add("is-invalid")
			document.getElementById("time").classList.add("is-invalid")
			document.getElementById("date").classList.add("is-invalid")
			document.getElementById("datetimecheckbox").classList.add("is-invalid")
			return false;
		}else return true;
	}
	else{
		return true;}
}

function validateDesc(desc){
	if (desc == undefined){return true;}
	if (desc.length>255){
		document.getElementById("decs_textarea").classList.add("is-invalid")
		return false;
	}
	return true;
}

export function clearTheValidations(){
	let error= document.getElementById("location-feedback")
	error.innerHTML=""
	const elements = document.querySelectorAll('.is-invalid');
	// Iterate over the elements and remove the class
	for(let i=0; i<elements.length;i++){
		elements[i].classList.remove('is-invalid');
	}
}

function getTheFormData(form){
	// Create a FormData object from the form
	const formData = new FormData(form);

	// Convert FormData to JSON
	const formDataObject = {};
	formData.forEach((value, key) => {
		formDataObject[key] = value;
	});
	// Convert data to JSON string
	return JSON.stringify(formDataObject)
}

function makePostRequest(submitForm){
	// Data to be sent in the POST request (can be a JSON object, FormData, etc.)
	let data= getTheFormData(submitForm)
	submitReport(data)
}


/**

 * This function is responsible for clearing the form and enables the location input field and unmutes the text.
 *
 * @param submitForm
 * @param inputField
 */
export function clearForm(submitForm,inputField){
	submitForm.reset();
	inputField.disabled = false;
	inputField.classList.remove("text-muted");
}