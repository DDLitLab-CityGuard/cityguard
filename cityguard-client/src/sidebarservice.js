import {fetchSingleEventInfo} from "./apiwrapper/cityguard-api.js";
import {Offcanvas} from "bootstrap";


function openSidebarandFillTheData(data){
    let offcanvas= new Offcanvas(document.getElementById("offCanvasForInfo"));
    offcanvas.show();
}
export function addEventListenerToMarkers(markers){
    for(let i = 0; i < markers.length; i++){
        let current = markers[i];
        current.on('click', function(e) {
            let customID = e.target.customID.toString(); //die id vom Marker wird geholt
            fetchSingleEventInfo(customID,(data) =>{
               openSidebarandFillTheData(data)
            });

        });
    }
}
