$(document).ready(function() {

    $(".checkbox-group input[type='checkbox']").on("change", function () {
        $(".checkbox-group input[type='checkbox']").not(this).prop("checked", false);
    });
    $("#contact-form").submit(function(event) {
        event.preventDefault();
        let valid = true;
        
        $(".error").text("");

        if (!$("#first-name").val().trim()) {
            $("#first-name").next(".error").text("First name is required");
            valid = false;
        }
        if (!$("#last-name").val().trim()) {
            $("#last-name").next(".error").text("Last name is required");
            valid = false;
        }
        if (!/^\d{10}$/.test($("#phone").val())) {
            $("#phone").next(".error").text("Enter a valid 10-digit phone number");
            valid = false;
        }
        if (!$("#email").val().match(/^\S+@\S+\.\S+$/)) {
            $("#email").next(".error").text("Enter a valid email");
            valid = false;
        }
        if (!$("#zip").val().trim().match(/^\d+$/)) {
            $("#zip").next(".error").text("Zip code must contain only numbers");
            valid = false;
        }
        if (!$("#state").val().trim()) {
            $("#state").next(".error").text("State is required");
            valid = false;
        }
        
        if (valid) {
            let formData = $(this).serialize();
            $.ajax({
                type: "POST",
                url: "submit-form.php",
                data: formData,
                success: function(response) {
                    alert("Form submitted successfully: " + response);
                }
            });
        }
    });
});