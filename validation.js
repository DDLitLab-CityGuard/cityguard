
function formIsInvalid(form){
    json=gettheformdata(form)
    json=JSON.parse(json)
    locaval=validateLocation(json.location,json.currentLocation)
    datetimeval=validateDateTime(json.date,json.time,json.currentDateTime)
    descval=validateDesc(json.desc)
 
    if (descval==true && datetimeval==true && locaval==true){
        return false
    }
    return true
  }



function validateLocation(location,checkBox){
    if (checkBox == undefined){
        let regex = /([+-]?(?=\.\d|\d)(?:\d+)?(?:\.?\d*))(?:[Ee]([+-]?\d+))?,([+-]?(?=\.\d|\d)(?:\d+)?(?:\.?\d*))(?:[Ee]([+-]?\d+))?/i;
        if (location==""){document.getElementById("location").classList.add("is-invalid")
        document.getElementById("locationcheckbox").classList.add("is-invalid")
        error =document.getElementById("location-feedback")
        var text = document.createTextNode("Please check the checkbox or type the coordinates");
        error.appendChild(text);
        return false;}
        if (location.match(regex)==null){
            document.getElementById("location").classList.add("is-invalid")
            document.getElementById("locationcheckbox").classList.add("is-invalid")
            error=document.getElementById("location-feedback")
            var text = document.createTextNode("Please correct the coordinates");
            error.appendChild(text);
            return false;
            
        }
    }
    else{
   return true;}
}



function validateDateTime(date,time,checkBox){
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

function validateDesc(desc){
    if (desc == undefined){return true;}
    if (desc.length>255){
            document.getElementById("decs_textarea").classList.add("is-invalid")
        return false;
    }
return true;
    
}



function clearTheValidations(){
   var elements = document.querySelectorAll('.is-invalid');
   console.log(elements)

    // Iterate over the elements and remove the class
    for(let i=0; i<elements.length;i++){
      elements[i].classList.remove('is-invalid');
    }
}



function closeTheModal(){
    submitButton.setAttribute("data-bs-dismiss","modal")
    submitButton.click();
}

