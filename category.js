reportButton=document.getElementById('report_button') 



//fetch categories from api
function fetchCategoriesAndRenderOptions() {
    select = document.getElementById('selectCategory');
    removeCategoryOptions(select);
    fetch('http://127.0.0.1:5123/api/fetch_categories')
        .then(response => response.json())
        .then(data => {
            for (var i = 0; i<data.length; i++){
                var opt = document.createElement('option');
                opt.value = data[i].id;
                opt.innerHTML = data[i].name;
                select.appendChild(opt);}
        });
}


reportButton.addEventListener('click', function(event){
    fetchCategoriesAndRenderOptions();

  });



  function removeCategoryOptions(select){
        // Loop through all the options in reverse order
        for (let i = select.options.length - 1; i >= 0; i--) {
          // Remove the option at index i
          select.remove(i);
        }
  }