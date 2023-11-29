import {Mapdisplayservice} from "./mapdisplayservice.js";
import {FormService} from "./formservice.js";
import {NavigationService} from "./navigationservice.js";

function main() {
    let reportButton = document.getElementById('reportButton');
    let submitButton = document.getElementById('submitButton');
    let closeButton = document.getElementById('closeModal');
    let checkbox = document.getElementById("locationcheckbox");
    let eingabeFeld = document.getElementById("location");
    let location_hidden = document.getElementById("location_hidden");
    let submitForm = document.getElementById('submit_form');
    let map = L.map('map').setView([53.566819239846915, 10.004717089957754], 13);
    let mapdisplayservice = new Mapdisplayservice(map);
    let categoryservice = new FormService(reportButton,submitButton,closeButton,submitForm);
    let navgationservice =new NavigationService(checkbox,eingabeFeld,location_hidden);
}

main()

