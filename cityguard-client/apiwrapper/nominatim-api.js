/**
 * This Module contains functions to communicate with the CityGuard API.
 * Whenever a different Module needs to communicate with the API, it should import this Module and call the functions defined here.
 * @module nominatim-api
 */

const geoCoderApiEndpoint =" https://cityguard.isa.uni-hamburg.de/nominatim/search?q="


/**
 * Fetches the geolocation objects which matches the locationString.
 * @param locationString
 * @param func
 */
export function fetchCoordinatesMatchingString(locationString,func) {
    fetch(geoCoderApiEndpoint + locationString)
        .then(response => response.json())
        .then(data => {
            func(data)

        });
}