import {fetchSingleEventInfo} from "./apiwrapper/cityguard-api.js";
import {Offcanvas} from "bootstrap";


function openSidebarandFillTheData(data){
    let offcanvas= new Offcanvas(document.getElementById("offCanvasForInfo"));
    let sidebartitle = document.getElementById("offcanvasScrollingLabel");
    let sidebarcategory = document.getElementById("sidebar-category");
    let sidebardescription = document.getElementById("sidebar-desc");
    let sidbardate = document.getElementById("sidebar-date");
    let sidebartime = document.getElementById("sidebar-time");
    sidebartitle.innerText = data.title;
    sidebarcategory.innerText = data.category;
    sidebardescription.innerText = data.description;
    sidbardate.innerText = data.date;
    sidebartime.innerText = data.time;

    offcanvas.show();
}
export function addEventListenerToMarkers(markers){
    for(let i = 0; i < markers.length; i++){
        let current = markers[i];
        current.on('click', function(e) {
            let customID = e.target.customID.toString(); //die id vom Marker wird geholt
            fetchSingleEventInfo(customID,(data) =>{
                console.log(data);
               openSidebarandFillTheData(data)
            });

        });
    }
}
