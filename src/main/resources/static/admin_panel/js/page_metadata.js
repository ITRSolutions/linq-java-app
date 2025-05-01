$(document).ready(function() {
    // Pre-fill the form with existing data (optional, based on your API response)
    $.get("/api/v1/pageMetadata", function(response) {
        if(response.status) {
            const data = response.data;
            // Pre-fill the form fields with existing data
            $("textarea[name='font_family']").val(data.fontFamily);
            $("input[name='bottom_header']").val(data.bottomHeader);
            $("input[name='fb_URL']").val(data.fbUrl);
            $("input[name='insta_URL']").val(data.instaUrl);
            $("input[name='x_URL']").val(data.xurl);
            $("input[name='login_URL']").val(data.loginUrl);
            $("input[name='logo_URL']").val(data.logoUrl);
            $("input[name='button_text']").val(data.buttonText);
            $("input[name='button_URL']").val(data.buttonUrl);
            $("input[name='address']").val(data.address);
            $("input[name='phone_number']").val(data.phoneNumber);
            $("input[name='email']").val(data.email);
            $("input[name='version']").val(data.version);
            $("textarea[name='seo_code']").val(data.seoCode);

            $("#updatedBy").html(data.updatedBy ? `${data.updatedBy.firstName} ${data.updatedBy.lastName}` : "N/A");
            $("#updatedAt").html(formatDate(data.updatedAt));

            let imgURL = correctURL(data.logoImgUrl);
//let imgURL = data.logoImgUrl;
            $("#loadLogoURL").attr("href", imgURL);
            $("#logo_img_url").val(imgURL);
        }
    });

//--------------Update form Start----------------------
    $('#pageMetaDataForm').on('submit', function(event) {
        event.preventDefault(); // Prevent the default form submission

        // Collect form data
        var formData = {
            fontFamily: $("textarea[name='font_family']").val(),
            bottomHeader: $("input[name='bottom_header']").val(),
            fbUrl: $("input[name='fb_URL']").val(),
            instaUrl: $("input[name='insta_URL']").val(),
            xurl: $("input[name='x_URL']").val(),
            loginUrl: $("input[name='login_URL']").val(),
            logoImgUrl: $("#logo_img_url").val(), // Hidden field holding the logo URL
            logoUrl: $("input[name='logo_URL']").val(),
            buttonText: $("input[name='button_text']").val(),
            buttonUrl: $("input[name='button_URL']").val(),
            address: $("input[name='address']").val(),
            phoneNumber: $("input[name='phone_number']").val(),
            email: $("input[name='email']").val(),
            version: $("input[name='version']").val(),
            seoCode: $("textarea[name='seo_code']").val()
        };

        // Show loading message (optional)
        $('#error-message').hide();
        $('#success-message').hide();

        // Send the PUT request with the collected data
        $.ajax({
            url: '/api/v1/pageMetadata',  // API URL to update the page metadata
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(formData),
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("X-CSRF-TOKEN", $("#_csrf").val());
                },
            success: function(response) {
                if (response.status) {
                    // Show success message
                    $('#success-message').text(response.message).show();
                    alert("Note: Any changes you make will take effect after 3 hours." +
                     "\nYou will not be able to see them on this form or the website immediately.");
                } else {
                    // Show error message
                    $('#error-message').text(response.message).show();
                }
            },
            error: function(xhr, status, error) {
                console.log('Error: ' + xhr.responseText);
                // Handle any error during the AJAX request
                $('#error-message').text('An error occurred while updating the data. Please try again.').show();
            }
        });
    });

//--------------Image Upload Start----------------------
        $(document).on('change', '#uploadImage', function () {
            // Get the selected file
            var file = this.files[0];

            // Check if a file is selected
            if (file) {
                var fileType = file.type; // Get the MIME type of the file
                var validImageTypes = ["image/jpeg", "image/png", "image/gif"]; // Valid image MIME types

                // Check if the file is an image and if it's in the valid list
                if ($.inArray(fileType, validImageTypes) === -1) {
                    $("#uploadImage").val(""); // Reset the file input
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
                        success: function(response) {
                            if (response.status) {
                                $('#updateSlideContent [type="submit"]').prop('disabled', false);
                                $("#success-message").hide();
                                $("#error-message").hide();
                                $("#success-message").text(response.message).show();
                                $("#loadLogoURL").show();
                                $('textarea[name="updateContentSG"]').val(response.data);

                                let imgURL = correctURL(response.data);
                                $("#loadLogoURL")[0].href = imgURL;
                                $("#logo_img_url").val(imgURL);
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
//--------------Image Upload End----------------------

    function correctURL(URL) {

    return (URL.startsWith("http") || URL.startsWith("/")) ? URL : "/" + URL;
}

    function formatDate(dateString) {
        var date = new Date(dateString);
        return date.toLocaleString();
    }

//--------------Display Company metaData start----------------------
        $.ajax({
            url: '/api/v1/pageMetadata/company', // API endpoint to fetch company metadata
            type: 'GET', // HTTP method
            success: function(response) {
                if (response.status) {
                    // Populate the form with the fetched data
                    var data = response.data;

                    // Set the form fields with the fetched data
                    $('input[name="companyName"]').val(data.companyName);
                    $('input[name="joinCompanyDesc"]').val(data.joinCompanyDesc);
                    $('input[name="participateNow"]').val(data.participateNowText);
                    $('input[name="buttonURL"]').val(data.buttonURL);
                } else {
                    // Handle if the response is not successful
                    $('#error-message1').text('Failed to load company data.').show();
                }
            },
            error: function(xhr, status, error) {
                // If there is an error in the request, display an error message
                $('#error-message1').text('Error fetching company data: ' + error).show();
            }
        });

//--------------Update Company metaData start----------------------
    // Attach a submit event to the form
    $('#companyMetadataForm').submit(function(event) {
        event.preventDefault();  // Prevent the form from submitting normally

        // Get the form data
        var formData = {
            companyName: $('input[name="companyName"]').val(),
            joinCompanyDesc: $('input[name="joinCompanyDesc"]').val(),
            participateNowText: $('input[name="participateNow"]').val(),
            buttonURL: $('input[name="buttonURL"]').val()
        };

        // Make an AJAX PUT request
        $.ajax({
            url: '/api/v1/pageMetadata/company', // Your API endpoint
            type: 'PUT', // HTTP method
            contentType: 'application/json', // Sending JSON data
            data: JSON.stringify(formData), // Convert the data to JSON string
            success: function(response) {
                // If the request is successful, show success message
                $('#success-message1').text(response.message).show();
                $('#error-message1').hide();
            },
            error: function(xhr, status, error) {
                // If the request fails, show error message
                $('#error-message1').text('Error updating company data: ' + error).show();
                $('#success-message1').hide();
            }
        });
    });
});
