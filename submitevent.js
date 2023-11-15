//csrftoken??
submitButton=document.getElementById('submitevent') 
submitForm=document.getElementById('submit_form')


  function gettheformdata(){
        // Create a FormData object from the form
  const formData = new FormData(submitForm);
  
// Convert FormData to JSON
const formDataObject = {};
formData.forEach((value, key) => {
  formDataObject[key] = value;
});
  jsondict=JSON.stringify(formDataObject) // Convert data to JSON string
    return jsondict
  }

  function makePostRequest(){
    // URL endpoint for the POST request
    const url = "http://127.0.0.1:5123/api/submit_report";
    // Data to be sent in the POST request (can be a JSON object, FormData, etc.)
    data=gettheformdata();

    // Fetch options
    const options = {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json', // Set the content type according to your data
    // You may need to include additional headers, such as authentication tokens
    // 'Authorization': 'Bearer YOUR_ACCESS_TOKEN'
    },
    body: jsondict
    };


    // Make the POST request
    fetch(url, options)
    .then(response => {
    // Check if the request was successful (status code 2xx)
    if (!response.ok) {
    throw new Error(`HTTP error! Status: ${response}`);
    }
    // Parse the response JSON
    return response.json();
    })
    .then(data => {
    // Handle the data returned by the server
    console.log(data);
    })
  .catch(error => {
  // Handle any errors that occurred during the fetch
    console.error('Fetch error:', error);
  });

  };

  submitButton.addEventListener('click', function(event){
    //make fetch post request
    makePostRequest();

    //clear the model form fields
    submitForm.reset()
  });