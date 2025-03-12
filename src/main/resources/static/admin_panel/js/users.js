 
    $('.openSlide').click(function(){
        $("#slides-tab").click();
    });  

    $('#addSlideFunction').click(function () {
        $('#addSlide').modal('show');
    });
    
    $('.updateUser').click(function () {
        $('#updateUser').modal('show');
    });

    $('.deleteUser').click(function(){
        if (confirm("Are you sure to delete this slide row!")) {
            
            
          }  
    });