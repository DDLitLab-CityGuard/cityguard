import {Mapdisplayservice} from "./mapdisplayservice";
import {FormService} from "./formservice";
import {NavigationService} from "./navigationservice";
class Application{

    constructor() {
        this.reportButton = document.getElementById('reportButton');
        this.submitButton = document.getElementById('submitButton');
        this.closeButton=document.getElementById('closeModal');
        this.checkbox = document.getElementById("locationcheckbox");
        this.eingabeFeld = document.getElementById("location");
        this.location_hidden = document.getElementById("location_hidden");
        this.submitForm=document.getElementById('submit_form');
        this.map= L.map('map').setView([53.566819239846915, 10.004717089957754], 13);
        this.mapdisplayservice = new Mapdisplayservice(this.map);
        this.categoryservice = new FormService(this.reportButton,this.submitButton,this.closeButton,this.submitForm);
        this.navgationservice=new NavigationService(this.checkbox,this.eingabeFeld,this.location_hidden);
    }

}