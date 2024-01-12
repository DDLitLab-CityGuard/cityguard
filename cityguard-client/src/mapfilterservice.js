import {fetchAndRenderReports} from "./mapdisplayservice.js";

export let display_heatmap = false;

export function displayMapFilterButton(filter_list, map, heatmapGroup, markerGroup) {
    const marker_filter = filter_list.filter(filter => filter.allowDiscrete);
    let style = `
        position: absolute; 
        top: 10px; 
        right: 10px; 
        z-index: 1000; 
        display: flex; 
        flex-direction: column;
        padding: 7px;
    `
    let div = document.createElement("div");
    div.setAttribute("style", style);
    let markerFilterMenu = generateMarkerFilter(marker_filter, map, heatmapGroup, markerGroup);
    let heatmapFilterMenu = generateHeatmapFilter(filter_list, map, heatmapGroup, markerGroup);
    div.appendChild(markerFilterMenu);
    div.appendChild(heatmapFilterMenu);
    document.body.appendChild(div);

    let heatmapToggle = document.getElementById("heatmapToggle");
    heatmapToggle.addEventListener("change", function(){
        display_heatmap = heatmapToggle.checked;
        fetchAndRenderReports(map, heatmapGroup, markerGroup);
    });

    for(let i = 0; i < marker_filter.length; i++){
        let filter_id = marker_filter[i].id;
        let filter = document.getElementById("markerFilter-" + filter_id);
        filter.addEventListener("change", function(){
            fetchAndRenderReports(map, heatmapGroup, markerGroup);
        });
    }
}


function generateHeatmapFilter(filter_list, map, heatmapGroup, markerGroup){
    let style = `
        margin-top: 10px;
        width: 140px;
        background-color: white; 
        padding: 7px;
        border-radius: 3px;
        box-shadow: 0 0 5px rgba(0,0,0,0.3);
    `

    let radio_buttons = ""
    for(let i = 0; i < filter_list.length; i++){
        let filter_name = filter_list[i].name;
        let id = filter_list[i].id;
        radio_buttons += `
        <div class="form-check">
            <input class="form-check-input" type="radio" name="flexRadioDefault" id="heatmapFilter-`+ id +`">
            <label class="form-check-label" for="heatmapFilter-`+ id +`">
                `+ filter_name +`
            </label>
        </div>
        `
    }

    let filter_menu = `
    <div id="filter-menu" style="` + style +`">
        <div style="margin: auto">
            <div class="form-check form-switch">
                <input class="form-check-input" type="checkbox" name="flexRadioDefault" id="heatmapToggle">
                <label class="form-check-label" for="heatmapToggle">
                    <h6>Heatmap</h6>
                </label>
            </div>
        </div>
        <hr style="margin-top: 0; margin-bottom: 3px"></hr>
        `+ radio_buttons +`
    </div>
    `
    const parser = new DOMParser();
    return parser.parseFromString(filter_menu, "text/html").getElementById("filter-menu");
}


function generateMarkerFilter(filter_list, map, heatmapGroup, markerGroup){
    let style = `
        width: 140px;
        background-color: white; 
        padding: 7px;
        border-radius: 3px;
        box-shadow: 0 0 5px rgba(0,0,0,0.3);
    `

    let radio_buttons = ""
    for(let i = 0; i < filter_list.length; i++){
        let filter_name = filter_list[i].name;
        let id = filter_list[i].id;
        radio_buttons += `
        <div class="form-check">
            <input class="form-check-input" type="checkbox" name="flexRadioDefault" id="markerFilter-`+ id +`">
            <label class="form-check-label" for="markerFilter-`+ id +`">
                `+ filter_name +`
            </label>
        </div>
        `
    }

    let filter_menu = `
    <div id="filter-menu" style="` + style +`">
        <div style="display: flex; justify-content: center">
            <h6>Filter</h6>
        </div>
        <hr style="margin-top: 0; margin-bottom: 3px"></hr>
        `+ radio_buttons +`
    </div>
    `

    const parser = new DOMParser();
    return parser.parseFromString(filter_menu, "text/html").getElementById("filter-menu");
}