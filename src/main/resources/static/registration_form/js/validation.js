			function validateForm() {
			    // Validate password match
			    var password = document.getElementById("password").value;
			    var confirmPassword = document.getElementById("confirmPassword").value;

			    if (password !== confirmPassword) {
			        $("#confirmPassword_error").text("Confirm password is not matching");
			        return false;
			    }

			    // If everything is valid, form can be submitted
			    return true;
			}

			// Function to clear error messages
			function clearErrorMessages() {
			    const errorElements = document.getElementsByClassName("error-message");
			    for (let element of errorElements) {
			        element.textContent = "";
			    }
			}

			// AJAX Form Submission
			$(document).ready(function() {
			    $("#personalDetailsForm").on('submit', function(e) {
			        e.preventDefault(); // Prevent default form submission

			        // Validate form
			        if (validateForm()) {

			            $("#submitButton").prop("disabled", true);
			            // Send AJAX request
			            $.ajax({
			                url: '/api/v1/auth/register', // The server-side URL to which you want to send the request
			                type: 'POST',
			                data: JSON.stringify(getFormData()), // Convert the form data to JSON
			                contentType: 'application/json', // Set content type to JSON
			                success: function(response) {
			                    $("#submitButton").prop("disabled", false);
			                    // Handle successful submission
			                    alert("Form submitted successfully!");
			                    $("#clearForm").click();
			                },
			                error: function(xhr, status, error) {
			                    // If the response is a validation error
			                    if (xhr.status) {
			                        var errors = xhr.responseJSON.errors;

			                        // Clear existing error messages before displaying new ones
			                        clearErrorMessages();

			                        // Loop through errors and show them under each input field
			                        for (var field in errors) {
			                            if (errors.hasOwnProperty(field)) {
			                                $("#" + field + "_error").text(errors[field][0]); // Display the first error message for each field
			                            }
			                        }
			                        $("#submitButton").prop("disabled", false);
			                    } else {
			                        // Handle other errors (like server errors, etc.)
			                        $("#submitButton").prop("disabled", false);
			                        alert("Something went wrong. Please try again.");
			                    }
			                }
			            });

			            // Function to get form data and convert it into a JSON object
			            function getFormData() {
			                var formData = {};
			                var formArray = $('#personalDetailsForm').serializeArray(); // Serialize the form into an array of name-value pairs

			                // Convert the serialized array into a JavaScript object
			                formArray.forEach(function(field) {
			                    formData[field.name] = field.value;
			                });

			                return formData;
			            }

			        }
			    });
			});