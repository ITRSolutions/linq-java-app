 
    $('.openSlide').click(function(){
        $("#slides-tab").click();
    });  

    $('#addSlideFunction').click(function () {
        $('#addSlide').modal('show');
    });
    
    $('.updateSlide').click(function () {
        $('#updateSlide').modal('show');
    });

    $('.deleteSlide').click(function(){
        if (confirm("Are you sure to delete this slide row!")) {
            
            
          }  
    });

