
const checkbox = document.getElementById("locationcheckbox");
const eingabeFeld = document.getElementById("location");
const location_hidden = document.getElementById("location_hidden");

checkbox.addEventListener("change", async function() {
  if (checkbox.checked) {
    eingabeFeld.disabled = true;
    eingabeFeld.classList.add("text-muted");
    const coordinates = await getCoordinates(); 
    eingabeFeld.value = `${coordinates.latitude},${coordinates.longitude}`;
    location_hidden.value = `${coordinates.latitude},${coordinates.longitude}`;
  } else {
    eingabeFeld.disabled = false;
    eingabeFeld.classList.remove("text-muted");
  }
});

function getCoordinates() {
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