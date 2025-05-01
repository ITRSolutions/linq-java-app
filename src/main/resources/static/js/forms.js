$(document).ready(function() {

    $(".checkbox-group input[type='checkbox']").on("change", function () {
        $(".checkbox-group input[type='checkbox']").not(this).prop("checked", false);
    });


// jQuery function to handle form submission
$('#contact-form').on('submit', function(event) {
    event.preventDefault(); // Prevent default form submission

    // Disable the submit button to prevent multiple submissions
    var submitButton = $('button[type="submit"]');
    submitButton.prop('disabled', true);

    var checkboxesExist = $('input[name="contact-time"]').length > 0;
    // Check if at least one checkbox is checked
    if (checkboxesExist && $('input[name="contact-time"]:checked').length === 0) {
        alert('Please select at least one preferred contact time.');
        submitButton.prop('disabled', false); // Re-enable the submit button if validation fails
        return; // Stop the form submission if no checkbox is checked
    }

    // Collect form data
    var formData = {
        firstName: $('#first-name').val(),
        lastName: $('#last-name').val(),
        dateOfBirth: $('#dob').val(),
        phoneNumber: $('#phone').val(),
        email: $('#email').val(),
        bestContactTime: $('input[name="contact-time"]:checked').val(),
        state: $('#state').val(),
        pageName: $('#pageName').val(),

        speciality: $('#speciality').val(),

        isEmployee: $('#isEmployee').val(),
        isStudyParticipant: $('#isStudyParticipant').val(),
        refName: $('#refName').val(),
        refContactNumber: $('#refContactNumber').val()
    };

    // Send the form data using AJAX
    $.ajax({
        url: '/api/v1/forms',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function(response) {
            // On successful form submission, display response message
            $('#response-message').text(response.message).css('color', 'green').show();
            $('form')[0].reset(); // Reset form
        },
        error: function(xhr, status, error) {
            // Handle any errors that occur during the request
            $('#response-message').text('An error occurred. Please try again.').css('color', 'red').show();
        },
        complete: function() {
            // Re-enable the submit button after the request is complete (whether successful or not)
            submitButton.prop('disabled', false);
        }
    });
});


        // Phone number validation to accept only numbers, "+" and "-" signs
        $('.phone').on('input', function() {
            var value = $(this).val();
            // Allow only digits, "+" and "-" characters
            if (!/^[-\+0-9]*$/.test(value)) {
                $(this).val(value.slice(0, -1)); // Remove the last character if it doesn't match
            }
        });
});