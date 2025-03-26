$(document).ready(function () {

    // Handle the form submission
    $('#updatePasswordForm').on('submit', function (event) {
        event.preventDefault();  // Prevent the default form submission

        // Get the password values
        var password = $('#password').val();
        var confirmPassword = $('#confirm_password').val();

        // Check if the passwords match
        if (password !== confirmPassword) {
            alert("Passwords do not match.");
            return; // Stop further execution
        }

        // Send the PUT request to update the password
        $.ajax({
            url: '/api/v1/auth/set-password',  // API endpoint
            type: 'PUT',
            dataType: 'json',
            contentType: 'application/json',
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("X-CSRF-TOKEN", $("#_csrf").val());
                },
            data: JSON.stringify({
                "password": password,
                "confirmPassword": confirmPassword
            }),
            success: function (response) {
                if (response.status) {
                    // Success - Password updated
                    alert(response.message); // Display success message
                    // Optionally reset the form or redirect to another page
                    $('#updatePasswordForm')[0].reset();  // Reset the form
                } else {
                    // Failure - Handle error (optional)
                    alert("Failed to update password. Please try again.");
                }
            },
            error: function (xhr, status, error) {
                // Error in the request
                alert("Error: " + error);
            }
        });
    });
});
