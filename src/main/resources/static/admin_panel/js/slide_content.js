 var SlideIdSC = 0;
 var SlideContentTable = null;
 var SlideNameSC = null;

    $(document).on('click', '.openSlideContent', function () {

        $("#slide_contents-tab").click();

        $("#SlideIdSC").val(0);
        let rowIndex = $(this).closest('tr').index();
        let selected = SlideTable[rowIndex];

        $("#SlideIdSC").val(selected.id);
        SlideIdSC = selected.id;
        fetchSlideContents();

        SlideNameSC = $(this).closest('td');
        SlideNameSC = SlideNameSC[0].children[0].textContent;
    });

$(document).on('click', '#addSlideContentFunction', function () {
    $('#addSlideContent').modal('show');
});

        $(document).on('click', '.updateSlideContent', function() {
            let rowIndex = $(this).closest('tr').index();
            let selected = SlideContentTable[rowIndex];

            $('#updateSlideContent [name="updateContentTypeSG"]').val(selected.contentType);
            !checkImageField() ? $('#updateSlideContent [name="updateContentSG"]').val(selected.content) : 0;
            $('#updateSlideContent [name="updateOrderIndexSC"]').val(selected.orderIndex);
            $("#SCrowId").val(selected.id);

            $('#updateSlideContent').modal('show');
        });

function checkImageField() {
    if($('select[name="updateContentTypeSG"]').val() == "IMAGE") {
        $(".contentSG").hide();
        $("#contentSCLb").hide();

        $(".imageUpload").html(`	<label>Upload Image: </label>
        <a href="#" target="_blank" class="float-right" id="displayImg" style="display: none"><label> [Uploaded Image]</label></a>
         	<input type="file" name="file" class="form-control1 control3 uploadImage" accept="image/*"
                 style="padding-top: 9px;" required>

                     <p id="error-message" style="color: red; display: none;"></p>
                     <p id="success-message" style="color: green; display: none;"></p>`);

         	return true;
    } else {
       $(".contentSG").show();
       $(".imageUpload").html("");
       $("#contentSCLb").show();
    }
}

$('.deleteSlideContent').click(function () {
    if (confirm("Are you sure to delete this slide content row!")) {


    }
});

$(document).ready(function () {
    $(document).on('change', '.uploadImage', function () {
        // Get the selected file
        var file = this.files[0];

        // Check if a file is selected
        if (file) {
            var fileType = file.type; // Get the MIME type of the file
            var validImageTypes = ["image/jpeg", "image/png", "image/gif"]; // Valid image MIME types

            // Check if the file is an image and if it's in the valid list
            if ($.inArray(fileType, validImageTypes) === -1) {
                $(".uploadImage").val(""); // Reset the file input
                alert("Please upload a valid image file (JPG, PNG, GIF, etc.).");
                return;
            } else if (file.size > 3 * 1024 * 1024) {
                $("#error-message").text("File size exceeds the 3MB limit.").show();
                return;
            }
        } else {
            return;
        }

                var formData = new FormData();
                formData.append("file", file);

                $('#updateSlideContent [type="submit"]').prop('disabled', true);
                $("#success-message").text("Uploading Image please wait....").show();
                $("#error-message").hide();

                // Make POST request to upload the image
                $.ajax({
                    url: '/api/v1/s3/upload',
                    type: 'POST',
                    data: formData,
                    contentType: false,
                    processData: false,
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("X-CSRF-TOKEN", $("#_csrf").val());
                },
                    success: function(response) {
                        if (response.status) {
                            $('#updateSlideContent [type="submit"]').prop('disabled', false);
                            $("#success-message").hide();
                            $("#error-message").hide();
                            $("#success-message").text(response.message).show();
                            $("#displayImg").show();
                            $("#displayImg")[0].href = response.data;
                            $('textarea[name="updateContentSG"]').val(response.data);
                        } else {
                            $("#success-message").hide();
                            $("#error-message").text(response.errors.Image[0]).show();
                        }
                    },
                    error: function() {
                        $("#success-message").hide();
                        $("#error-message").text("An error occurred while uploading the image.").show();
                    }
                });

    }); 

  //<------------Add Slide Content Start------------->
     $('#addSlideContent form').on('submit', function(e) {
         e.preventDefault(); // Prevent default form submission

         // Collect the form data
         var slideId = $('#SlideIdSC').val(); // Get the Slide ID (hidden input)
         var contentType = $('select[name="contentTypeSG"]').val(); // Get the content type (Button/Text/Image/URL/Disease)
         var slideContent = $('textarea[name="contentSG"]').val(); // Get the slide content text
         var orderIndex = $('input[name="orderIndexSC"]').val(); // Get the order index (position)

         // Prepare the data to be sent in the request
         var data = {
             slideId: slideId,
             contentType: contentType,
             content: slideContent,
             orderIndex: orderIndex
         };

         // Send the data via a POST request to the server
         $.ajax({
             url: '/api/v1/slideContent', // URL to POST data to
             type: 'POST',
             contentType: 'application/json', // Set content type to JSON
             data: JSON.stringify(data), // Convert data object to JSON string
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("X-CSRF-TOKEN", $("#_csrf").val());
                },
             success: function(response) {
                 if (response.status) {
                    fetchSlideContents();
                     alert('Slide content created successfully!');
                     $('#addSlideContent').modal('hide'); // Close the modal after successful submission
                     // Optionally reset the form here
                     $('#addSlideContent form')[0].reset();
                 } else {
                     alert('Failed to create slide content. Please try again.');
                 }
             },
             error: function(xhr, status, error) {
                 alert('Error occurred while creating slide content.');
             }
         });
     });
  //<------------Add Slide Content END------------->

   //<------------Update Slide Content Start------------->
     // When the form is submitted
     $('#updateSlideContent form').on('submit', function(event) {
         event.preventDefault(); // Prevent default form submission

         // Collect form data
         var formData = {
             orderIndex: $('input[name="updateOrderIndexSC"]').val(),
             contentType: $('select[name="updateContentTypeSG"]').val(),
             content: $('textarea[name="updateContentSG"]').val() || '' // If textarea is not visible, fallback to empty
         };

         // Check if the required fields are valid
         if (formData.contentType && formData.content) {
             // Make the PUT request
             $.ajax({
                 url: '/api/v1/slideContent/' + $('#SCrowId').val(),
                 type: 'PUT',
                 contentType: 'application/json',
                 data: JSON.stringify(formData),
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("X-CSRF-TOKEN", $("#_csrf").val());
                },
                 success: function(response) {
                     // Handle successful response
                     if (response.status) {
                         fetchSlideContents();
                         alert(response.message);
                         $('#updateSlideContent').modal('hide');

                         $(".contentSG").show();
                         $(".imageUpload").html("");
                         $("#contentSCLb").show();
                     } else {
                         alert('Failed to update slide content. Please try again.');
                     }
                 },
                 error: function(xhr, status, error) {
                     // Handle error response
                     alert('Error: ' + error);
                 }
             });
         } else {
             alert("Please fill in all required fields.");
         }
     });
  //<------------Update Slide Content End------------->
});

  function fetchSlideContents() {
    $.ajax({
      url: '/api/v1/slideContent/'+SlideIdSC, // URL to fetch all slide content
      type: 'GET',
      success: function(response) {
        if (response.status) {
          var data = response.data;
          var tableBody = $('#slideContentTableBody');
          tableBody.empty(); // Clear any existing data

          SlideContentTable = data;
          // Iterate through the data and create table rows
          data.forEach(function(item) {
            var row = `
              <tr>
                <td>${item.orderIndex}</td>
                <td>${item.contentType}</td>
                <td>${truncateText(item.content, item.contentType, 40)}</td>
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

    function truncateText(text, type, length) {
        let textC = text.length > length ? text.substring(0, length) + '...' : text;

if (type == "IMAGE" || type == "URL") {
    let hrefs = (text.startsWith("http") || text.startsWith("/")) ? text : "/" + text;

    let anchorTag = $('<a>', {
        text: textC,
        href: hrefs,
        target: '_blank',
        rel: 'noopener noreferrer'
    })[0].outerHTML;

    return anchorTag;
}

        return textC;
}
