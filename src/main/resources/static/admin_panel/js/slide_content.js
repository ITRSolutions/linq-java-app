$('.openSlideContent').click(function () {
    $("#slide_contents-tab").click();
});

$('#addSlideContentFunction').click(function () {
    $('#addSlideContent').modal('show');
});

$('.updateSlideContent').click(function () {
    $('#updateSlideContent').modal('show');
});

$('.deleteSlideContent').click(function () {
    if (confirm("Are you sure to delete this slide content row!")) {


    }
});


$(document).ready(function () {
    $(".uploadImage").change(function () {
        // Get the selected file
        var file = this.files[0];

        // Check if a file is selected
        if (file) {
            var fileType = file.type; // Get the MIME type of the file
            var validImageTypes = ["image/jpeg", "image/png", "image/gif"]; // Valid image MIME types

            // Check if the file is an image and if it's in the valid list
            if ($.inArray(fileType, validImageTypes) === -1) {
                // If not a valid image, show error and alert
                $("#errorMessage").show();
                $(".uploadImage").val(""); // Reset the file input
                alert("Please upload a valid image file (JPG, PNG, GIF, etc.).");
            } else {
                // If it's a valid image, hide the error message
                $("#errorMessage").hide();
            }
        }
    }); 


    
// Initialize the Quill editor
// var quill = new Quill('#editor', {
//     theme: 'snow'
//   });

  const toolbarOptions = [
    ['bold', 'italic', 'underline'],  
    ['link'],
  
    [{ 'header': 1 }, { 'header': 2 }],    
    [{ 'script': 'sub'}, { 'script': 'super' }],                        
   
    [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
  
  
    ['clean']                                         
  ];
  
  const quill = new Quill('#editor', {
    modules: {
      toolbar: toolbarOptions
    },
    theme: 'snow'
  });


  $('#custom-color-button').on('click', function() {
    $('#color-picker').click();
  });

  $('#color-picker').on('input', function() {
    var color = $(this).val();
    var range = quill.getSelection();
    if (range) {
      quill.format('color', color);
    }
    $('#custom-color-button').css('background-color', color);
  });
 
 
});

// Function to get the content of the editor as HTML and set it to the textarea
function getEditorContent() {
    var content = quill.root.innerHTML;
    $('#output').val(content);
}