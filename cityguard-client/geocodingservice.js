
import {fetchCoordinatesMatchingString} from "./api/cityguard-api.js";
    export function fetchCoordinatesFromInput(inputField, e) {
        let hiddenInputField = document.getElementById("location_hidden");
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
$(document).ready(function() {
    console.log("Hallo City test")
});