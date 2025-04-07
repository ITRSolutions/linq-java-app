$(document).ready(function () {
    // Handle form submission
    $('#customerEnquiryForm').on('submit', function (e) {
        e.preventDefault(); // Prevent default form submission

        // Get form values
        const firstName = $('#firstName').val().trim();
        const lastName = $('#lastName').val().trim();
        const email = $('#email').val().trim();
        const phone = $('#phone').val().trim();
        const message = $('#message').val().trim();

        // Validate form fields
        if (firstName.length < 1 || lastName.length < 1 || email.length < 5 || phone.length < 10 || message.length < 1) {
            $('#responseMessage').text('Please fill in all required fields.').css('color', 'red').show();
            return;
        }

        $('button[type="submit"]').prop('disabled', true);

        // Make the API request using AJAX
        $.ajax({
            url: '/api/v1/forms/customer_enquiry',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                firstName: firstName,
                lastName: lastName,
                email: email,
                phone: phone,
                message: message
            }),
            success: function (response) {
                if (response.status) {
                    $('#responseMessage').text('Form submitted successfully.').css('color', 'green').show();
                    $('#customerEnquiryForm')[0].reset(); // Reset form
                    $('button[type="submit"]').prop('disabled', false);
                } else {
                    $('#responseMessage').text('Error submitting form. Please try again later.').css('color', 'red').show();
                }
            },
            error: function () {
                $('#responseMessage').text('There was an error submitting the form. Please try again later.').css('color', 'red').show();
            }
        });
    });
});
