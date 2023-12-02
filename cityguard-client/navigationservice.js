export function getCoordinates() {
    if (navigator.geolocation) {
        return new Promise((resolve) => {
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