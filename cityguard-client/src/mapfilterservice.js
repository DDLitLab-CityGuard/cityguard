export function displayMapFilterButton(filter_list) {
    let filter_bar = document.createElement("div");
    filter_bar.setAttribute("style", "position: absolute; top: 10px; right: 10px; z-index: 1000; display: flex; flex-direction: row;")

    for (let i = 0; i < filter_list.length; i++) {
        let checkbox = document.createElement("div");
        checkbox.setAttribute("class", "form-check");
        let style = "background-color: white; padding: 5px; border-radius: 20px; padding-left: 35px; padding-right: 10px; box-shadow: 0px 2px 0px 0px rgba(0,0,0,0.2);";
        style += "margin-left: 10px";
        checkbox.setAttribute("style", style);

        let checkbox_input = document.createElement("input");
        checkbox_input.setAttribute("class", "form-check-input");
        checkbox_input.setAttribute("type", "checkbox");
        checkbox_input.setAttribute("value", "");
        checkbox_input.setAttribute("id", "category-filter-" + filter_list[i].id);
        checkbox.appendChild(checkbox_input);

        let checkbox_label = document.createElement("label");
        checkbox_label.setAttribute("class", "form-check-label");
        checkbox_label.setAttribute("for", "flexCheckDefault");
        checkbox_label.innerHTML = filter_list[i].name;
        checkbox.appendChild(checkbox_label);
        filter_bar.appendChild(checkbox);

        document.body.appendChild(filter_bar);
    }

}