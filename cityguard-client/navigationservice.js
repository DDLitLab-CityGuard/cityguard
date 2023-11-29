export class NavigationService{
    constructor(checkbox,eingabefeld,location_hidden){
        this.checkbox =checkbox
        this.eingabeFeld=eingabefeld
        this.location_hidden=location_hidden
        this.checkbox.addEventListener("change", this.checkboxChanged.bind(this));
    }


    async checkboxChanged() {
        if (this.checkbox.checked) {
            this.eingabeFeld.disabled = true;
            this.eingabeFeld.classList.add("text-muted");
            const coordinates = await this.getCoordinates();
            this.eingabeFeld.value = `${coordinates.latitude},${coordinates.longitude}`;
            this.location_hidden.value = `${coordinates.latitude},${coordinates.longitude}`;
        } else {
            this.eingabeFeld.disabled = false;
            this.eingabeFeld.classList.remove("text-muted");
        }
    }

    getCoordinates() {
        if (navigator.geolocation) {
            return new Promise((resolve, reject) => {
                navigator.geolocation.getCurrentPosition(function(position) {
                    const latitude = position.coords.latitude;
                    const longitude = position.coords.longitude;
                    resolve({ latitude, longitude });
                });
            });
        } else {
            alert("Geolokalisierung wird in diesem Browser nicht unterstützt.");
            return null;
        }
    }
}