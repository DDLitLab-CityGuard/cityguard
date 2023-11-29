export class FormService {
    constructor(reportButton, submitButton,closeButton,submitForm) {
        this.submitForm=submitForm
        this.closeButton=closeButton
        console.log(reportButton)
        reportButton.addEventListener('click', this.fetchCategoriesAndRenderOptions.bind(this));
        submitButton.addEventListener('click', function (e){
            this.fetchCategoriesAndRenderOptions(e)
        }.bind(this));
        //TODO check if event is not null

    }

    validationAndSubmit(event){
        console.log(event)
        event.preventDefault();
        this.clearTheValidations();

        if (this.formIsInvalid(this.submitForm)) {

        }
        else {
            //make fetch post request
            this.makePostRequest();
            this.closeTheModal();

            //clear the model form fields
            this.submitForm.reset()

        }
    }

    removeCategoryOptions(select){
        // Loop through all the options in reverse order
        for (let i = select.options.length - 1; i >= 0; i--) {
            // Remove the option at index i
            select.remove(i);
        }
    }


    //fetch categories from api
    fetchCategoriesAndRenderOptions() {
        let select = document.getElementById('selectCategory');
        this.removeCategoryOptions(select);
        fetch(document.location.protocol+"//"+document.location.hostname+":5123/api/fetch_categories")

            .then(response => response.json())
            .then(data => {
                for (let i = 0; i<data.length; i++){
                    let opt = document.createElement('option');
                    opt.value = data[i].id;
                    opt.innerHTML = data[i].name;
                    select.appendChild(opt);}
            });
    }

    formIsInvalid(form){
        let json=this.gettheformdata(form)
        json=JSON.parse(json)
        let locaval=this.validateLocation(json.location,json.currentLocation)
        let datetimeval=this.validateDateTime(json.date,json.time,json.currentDateTime)
        let descval=this.validateDesc(json.desc)

        if (descval==true && datetimeval==true && locaval==true){
            return false
        }
        return true
    }

    validateLocation(location,checkBox){
        if (checkBox == undefined){
            let regex =/[0-9]*\.[0-9]+,[0-9]*\.[0-9]+/i;
            if (location==""){
                document.getElementById("location").classList.add("is-invalid")
                document.getElementById("locationcheckbox").classList.add("is-invalid")
                let error =document.getElementById("location-feedback")
                var text = document.createTextNode("Please check the checkbox or type the coordinates");
                error.appendChild(text);
                return false;
            } else {
                if(location.match(regex)==null){
                    document.getElementById("location").classList.add("is-invalid")
                    document.getElementById("locationcheckbox").classList.add("is-invalid")
                    let error=document.getElementById("location-feedback")
                    let text = document.createTextNode("Please correct the coordinates");
                    error.appendChild(text);
                    return false;}

                return true;
            }
        } else {
            return true;
        }
    }

    validateDateTime(date,time,checkBox){
        if (checkBox == undefined){
            if (date=="" && time==""){//TODO regex
                document.getElementById("datetimegroup").classList.add("is-invalid")
                document.getElementById("time").classList.add("is-invalid")
                document.getElementById("date").classList.add("is-invalid")
                document.getElementById("datetimecheckbox").classList.add("is-invalid")
                return false;
            }else return true;
        }
        else{
            return true;}
    }

    validateDesc(desc){
        if (desc == undefined){return true;}
        if (desc.length>255){
            document.getElementById("decs_textarea").classList.add("is-invalid")
            return false;
        }
        return true;
    }

    clearTheValidations(){
        let error=document.getElementById("location-feedback")
        error.innerHTML=""
        var elements = document.querySelectorAll('.is-invalid');

        // Iterate over the elements and remove the class
        for(let i=0; i<elements.length;i++){
            elements[i].classList.remove('is-invalid');
        }
    }

    closeTheModal(){
        this.closeButton.click()
    }

    gettheformdata(form){
        // Create a FormData object from the form
        const formData = new FormData(form);

        // Convert FormData to JSON
        const formDataObject = {};
        formData.forEach((value, key) => {
            formDataObject[key] = value;
        });
        // Convert data to JSON string
        return JSON.stringify(formDataObject)
    }

    makePostRequest(){
        // URL endpoint for the POST request
        const url = document.location.protocol+"//"+document.location.hostname+":5123/api/submit_report";

        // Data to be sent in the POST request (can be a JSON object, FormData, etc.)
        let data=this.gettheformdata(this.submitForm);

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
            })
            .catch(error => {
                // Handle any errors that occurred during the fetch
            });

    };



}