 var SlideIdSC = 0;
 var SlideContentTable = null;
 var SlideNameSC = null;

    $(document).on('click', '.openSlideContent', function () {
        $("#slide_contents-tab").click();

        $("#SlideIdSC").val(0);
        let rowIndex = $(this).closest('tr').index();
        let selected = SlideTable[rowIndex];

        fetchSlides(selected.id);
        $("#SlideIdSC").val(selected.id);
        SlideIdSC = selected.id;
        fetchSlideContents();

        SlideNameSC = $(this).closest('td');
        SlideNameSC = SlideNameSC[0].children[0].textContent;
    });

$(document).on('click', '#addSlideContentFunction', function () {
    $('#addSlideContent').modal('show');
});

$(document).on('click', '.updateSlideContent', function () {
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

  //<------------Add Slide Content Start------------->
     $('#addSlideContent form').on('submit', function(e) {
         e.preventDefault(); // Prevent default form submission

         // Collect the form data
         var slideId = $('#SlideIdSC').val(); // Get the Slide ID (hidden input)
         var contentType = $('select[name="contentTypeSG"]').val(); // Get the content type (Button/Text/Image/URL/Disease)
         var slideContent = $('textarea[name="contentSG"]').val(); // Get the slide content text
         var imageAlt = $('input[name="imageAlt"]').val(); // Get the image alt text
         var orderIndex = $('input[name="orderIndexSC"]').val(); // Get the order index (position)

         // Prepare the data to be sent in the request
         var data = {
             slideId: slideId,
             contentType: contentType,
             content: slideContent,
             imageAlt: imageAlt,
             orderIndex: orderIndex
         };

         // Send the data via a POST request to the server
         $.ajax({
             url: '/api/v1/slideContent', // URL to POST data to
             type: 'POST',
             contentType: 'application/json', // Set content type to JSON
             data: JSON.stringify(data), // Convert data object to JSON string
             success: function(response) {
                 if (response.status) {
                     alert('Slide content created successfully!');
                     $('#addSlideContent').modal('hide'); // Close the modal after successful submission
                     // Optionally reset the form here
                     $('#addSlideContent form')[0].reset();
                 } else {
                     alert('Failed to create slide content. Please try again.');
                 }
             },
             error: function() {
                 alert('Error occurred while creating slide content.');
             }
         });
     });
  //<------------Add Slide Content END------------->
 
 
});

  function fetchSlideContents() {
    $.ajax({
      url: 'http://localhost:8090/api/v1/slideContent/'+SlideIdSC, // URL to fetch all slide content
      type: 'GET',
      success: function(response) {
        if (response.status) {
          var data = response.data;
          var tableBody = $('#slideContentTableBody');
          tableBody.empty(); // Clear any existing data

          // Iterate through the data and create table rows
          data.forEach(function(item) {
            var row = `
              <tr>
                <td>${item.orderIndex}</td>
                <td>${item.contentType}</td>
                <td>${truncateText(item.content, 40)}</td>
                <td>${item.slide.slideTitle}</td>
                <td>${item.slide.contentBlock.content}</td>
                <td>${item.slide.contentBlock.page.slug}</td>
                <td>${formatDate(item.updatedAt)}</td>
                <td>${item.updatedBy ? item.updatedBy.firstName + ' ' + item.updatedBy.lastName : 'N/A'}</td>
                <td>
                  <i class="fa fa-edit text-success text-active updateSlideContent"></i> <br>
                  <i class="fa fa-times text-danger text deleteSlideContent" style="display:none"></i>
                </td>
              </tr>
            `;
            tableBody.append(row);
          });
        } else {
          alert('Error fetching data');
        }
      },
      error: function() {
        alert('Error fetching data');
      }
    });
  }

  // Helper function to format the date
  function formatDate(dateString) {
    var date = new Date(dateString);
    return date.toLocaleString();
  }

    function truncateText(text, length) {
        return text.length > length ? text.substring(0, length) + '...' : text;
    }
