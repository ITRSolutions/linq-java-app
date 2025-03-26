 var CBIdSlide = 0;
 var SlideTable = null;
 var CBNameSlide = null;

    $(document).on('click', '.openSlide', function () {
        $("#slides-tab").click();

        $("#CBIdSlide").val(0);
        let rowIndex = $(this).closest('tr').index();
        let selectedCB = contentBlockTable[rowIndex];

        fetchSlides(selectedCB.id);
        $("#CBIdSlide").val(selectedCB.id);
        CBIdSlide = selectedCB.id;

        CBNameSlide = $(this).closest('td');
        CBNameSlide = CBNameSlide[0].children[0].textContent;
    });  

    $(document).on('click', '#addSlideFunction', function () {
        if(!CBNameSlide) {
            alert("No page is linked.");
            $("#Pages-tab").click();
            return 0;
        }

        $('#addSlide').modal('show');
        $('#addSlide').find('[name="CBName"]').val(CBNameSlide);
    });


        $(document).on('click', '.updateSlide', function() {
            const slideId = $(this).data('slide-id');

            let rowIndex = $(this).closest('tr').index();
            let selectedSlide = SlideTable[rowIndex];

            let CBName = $("#contentBlockTable").find("td")[2].textContent;

            $('#updateSlide [name="updateCBName"]').val(CBName);
            $('#updateSlide [name="updateSlideTitle"]').val(selectedSlide.slideTitle);
            $('#updateSlide [name="updateSlideOrderIndex"]').val(selectedSlide.orderIndex);
            $("#sliderowId").val(selectedSlide.id);

            $('#updateSlide').modal('show');
        });

    $('.deleteSlide').click(function(){
        if (confirm("Are you sure to delete this slide row!")) {

          }  
    });

 //<------------fetch Slides Start------------->
function fetchSlides(id) {
    // URL for the API call
    const apiUrl = "/api/v1/slide/"+id;

    // Make the AJAX call to get the data
    $.ajax({
        url: apiUrl,
        type: "GET",
        success: function(response) {
            // Check if the response is successful and contains data
            if (response.status && response.data.length > 0) {
                renderTableSlide(response.data);
                SlideTable = response.data;
            } else {
                $("#slideTable tbody").html("<tr><td colspan='6' class='text-center'>No slide found</td></tr>");
            }
        },
        error: function(err) {
            console.error('Error:', err);
        }
    });
}

function renderTableSlide(data) {
                let rows = '';

                // Iterate over the data array to populate rows
                data.forEach(function(item) {
                    const contentBlock = item.contentBlock; // Get the content block details
                    const updatedBy = item.updatedBy ? item.updatedBy.firstName + ' ' + item.updatedBy.lastName : 'N/A';
                    const updatedAt = new Date(item.updatedAt).toLocaleString();

                    // Create a new table row for each item
                    rows += `
                        <tr>
                            <td>${item.orderIndex}</td>
                            <td class="hidden-xs">
                                <a href="#" onclick="return false;" class="openSlideContent">
                                    ${item.slideTitle}
                                </a>
                            </td>
                            <td>
                                ${contentBlock.content}
                            </td>
                            <td>
                                ${contentBlock.page.slug}
                            </td>
                            <td>
                                ${updatedAt}
                            </td>
                            <td>
                                ${updatedBy}
                            </td>
                            <td>
                                <i class="fa fa-edit text-success text-active updateSlide" data-id="${item.id}"></i> <br>
                                <i class="fa fa-times text-danger text deleteSlide" data-id="${item.id}" style="display:none"></i>
                            </td>
                        </tr>
                    `;
                });

                // Append all rows to the tbody of the table
                $("#slideTable tbody").html(rows);
}
 //<------------fetch Slides End------------->


$(document).ready(function() {

 //<------------Add Slides Start------------->
    // Handle form submission for adding a slide
    $('#addSlide form').on('submit', function(event) {
        event.preventDefault(); // Prevent default form submission

        // Get form values
        const contentBlockId = $('#CBIdSlide').val(); // Assuming this is the ID of the content block
        const slideTitle = $('input[name="slideTitle"]').val();
        const slideOrderIndex = $('input[name="slideOrderIndex"]').val();

        // Prepare the data to send to the API
        const requestData = {
            slideTitle: slideTitle,
            contentBlockId: contentBlockId,   // Content Block ID (you can update it dynamically)
            orderIndex: slideOrderIndex
        };

        // Send POST request to create a new slide
        $.ajax({
            url: '/api/v1/slide',       // URL for the API endpoint
            type: 'POST',               // HTTP method
            contentType: 'application/json', // Content type header
            data: JSON.stringify(requestData), // Send data as JSON
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("X-CSRF-TOKEN", $("#_csrf").val());
                },
            success: function(response) {
                // Check if the response status is true
                if (response.status) {
                    alert(response.message);
                    fetchSlides(CBIdSlide);
                    $('#addSlide').modal('hide'); // Close the modal
                    $('#addSlide form')[0].reset(); // Reset the form
                } else {
                    alert('Error: ' + response.message); // Handle any errors from the API
                }
            },
            error: function(xhr, status, error) {
                // Handle any request errors
                console.error('Error:', error);
                alert('Error: Unable to create the slide.');
            }
        });
    });
     //<------------Add Slides End------------->


 //<------------Update Slides Start------------->
   // Handle form submission for updating the slide
    $('#updateSlide form').on('submit', function(event) {
        event.preventDefault(); // Prevent default form submission

        const slideId = $("#sliderowId").val(); // Get the slideId from the form data attribute

        const updateData = {
            slideTitle: $('input[name="updateSlideTitle"]').val(),
            orderIndex: $('input[name="updateSlideOrderIndex"]').val(),
        };

        // Send PUT request to update the slide
        $.ajax({
            url: `/api/v1/slide/${slideId}`, // URL to update the slide, including the slide ID
            type: 'PUT',
            contentType: 'application/json', // Content type header for JSON
            data: JSON.stringify(updateData), // Convert the data to JSON
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("X-CSRF-TOKEN", $("#_csrf").val());
                },
            success: function(response) {
                if (response.status) {
                    alert(response.message); // Show success message
                    $('#updateSlide').modal('hide'); // Close the modal
                    $('#updateSlide form')[0].reset(); // Reset the form
                    // Optionally, update the UI with the new slide data
                    fetchSlides(CBIdSlide);
                } else {
                    alert('Error: ' + response.message); // Show error message
                }
            },
          error: function(xhr, status, error) {
            // Try to parse the response as JSON
            try {
              var response = JSON.parse(xhr.responseText);
              // Check if the message property exists and display it in the alert
              if (response.message) {
                alert("Error: " + response.message);
              } else {
                alert("An unknown error occurred.");
              }
            } catch (e) {
              // If JSON parsing fails, display a generic error
              alert('Error: Unable to update the slide.');
            }
          }
        });
    });
     //<------------Update Slides End------------->

});

