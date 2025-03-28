const otpBtn = `<button type="submit" id="verifyOTP" class="login-btn d-block w-100 mt-4 fw-medium mb-2">Verify OTP</button>`;

const newPasswordBtn = `<button type="submit" id="submitButton" class="login-btn d-block w-100 mt-4 fw-medium mb-2">Submit</button>`;

const otpBlock = `<div class="mt-3 login-input" id="OtpCodeBlock">
    <svg class="start-icon"  width="20" height="20" viewBox="0 0 18 14" fill="none"  xmlns="http://www.w3.org/2000/svg">
        <path d="M3.5 6C3.22386 6 3 6.22386 3 6.5V8.5C3 8.77614 3.22386 9 3.5 9C3.77614 9 4 8.77614 4 8.5V6.5C4 6.22386 3.77614 6 3.5 6Z" fill="#838383"/>
        <path d="M11 7H11.5C11.7761 7 12 6.77614 12 6.5C12 6.22386 11.7761 6 11.5 6H11V7Z" fill="#838383"/>
        <path fill-rule="evenodd" clip-rule="evenodd" d="M0 1.5C0 0.671573 0.671573 0 1.5 0H13.5C14.3284 0 15 0.671573 15 1.5V13.5C15 14.3284 14.3284 15 13.5 15H1.5C0.671573 15 0 14.3284 0 13.5V1.5ZM2 6.5C2 5.67157 2.67157 5 3.5 5C4.32843 5 5 5.67157 5 6.5V8.5C5 9.32843 4.32843 10 3.5 10C2.67157 10 2 9.32843 2 8.5V6.5ZM7 6H6V5H9V6H8V10H7V6ZM10 5H11.5C12.3284 5 13 5.67157 13 6.5C13 7.32843 12.3284 8 11.5 8H11V10H10V5Z" fill="#838383"/>
    </svg>
    <input type="text" id="OtpCode" name="otpCode" placeholder="OTP Code" class="password-input" minlength="4" maxlength="4" onclick="restrictNumericInput('#OtpCode', 4)" required>
                                    </div>`;

const newPasswordBlock = `  <div class="mt-4 login-input">
                                    <svg class="start-icon" xmlns="http://www.w3.org/2000/svg" width="14" height="16" viewBox="0 0 14 16" fill="none">
                                        <path d="M7 12.1905C7.46413 12.1905 7.90925 12.0299 8.23744 11.7442C8.56563 11.4584 8.75 11.0708 8.75 10.6667C8.75 10.2625 8.56563 9.87494 8.23744 9.58917C7.90925 9.3034 7.46413 9.14286 7 9.14286C6.53587 9.14286 6.09075 9.3034 5.76256 9.58917C5.43437 9.87494 5.25 10.2625 5.25 10.6667C5.25 11.0708 5.43437 11.4584 5.76256 11.7442C6.09075 12.0299 6.53587 12.1905 7 12.1905ZM12.25 5.33333C12.7141 5.33333 13.1592 5.49388 13.4874 5.77965C13.8156 6.06542 14 6.453 14 6.85714V14.4762C14 14.8803 13.8156 15.2679 13.4874 15.5537C13.1592 15.8395 12.7141 16 12.25 16H1.75C1.28587 16 0.840752 15.8395 0.512563 15.5537C0.184374 15.2679 0 14.8803 0 14.4762V6.85714C0 6.453 0.184374 6.06542 0.512563 5.77965C0.840752 5.49388 1.28587 5.33333 1.75 5.33333H2.625V3.80952C2.625 2.79918 3.08594 1.83021 3.90641 1.11578C4.72688 0.401359 5.83968 0 7 0C7.57453 0 8.14344 0.0985363 8.67424 0.289983C9.20504 0.481429 9.68734 0.762037 10.0936 1.11578C10.4998 1.46953 10.8221 1.88949 11.042 2.35168C11.2618 2.81387 11.375 3.30925 11.375 3.80952V5.33333H12.25ZM7 1.52381C6.30381 1.52381 5.63613 1.76463 5.14384 2.19328C4.65156 2.62193 4.375 3.20331 4.375 3.80952V5.33333H9.625V3.80952C9.625 3.20331 9.34844 2.62193 8.85616 2.19328C8.36387 1.76463 7.69619 1.52381 7 1.52381Z" fill="#838383"></path>
                                    </svg>
                                    <div class="password-wrapper h-100">
                                        <input type="password" name="newPassword" id="newPassword" placeholder="New Password" class="password-input" required minlength="6" maxlength="20" title="New Password should be at least 6 characters long">
                                        <button class="end-icon" id="eye1" type="button" onclick="togglePasswordVisibility('newPassword', 'eye1')">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="16" viewBox="0 0 20 16" fill="none">
                                                <path d="M0.909091 1.12889L2.07273 0L17.2727 14.8622L16.1182 16L13.3182 13.2622C12.2727 13.6 11.1636 13.7778 10 13.7778C5.45455 13.7778 1.57273 11.0133 0 7.11111C0.627273 5.54667 1.62727 4.16889 2.9 3.07556L0.909091 1.12889ZM10 4.44444C10.7233 4.44444 11.417 4.7254 11.9285 5.22549C12.4399 5.72559 12.7273 6.40387 12.7273 7.11111C12.7277 7.41384 12.6755 7.71443 12.5727 8L9.09091 4.59556C9.38297 4.4951 9.69039 4.444 10 4.44444ZM10 0.444444C14.5455 0.444444 18.4273 3.20889 20 7.11111C19.2576 8.95372 17.9969 10.5532 16.3636 11.7244L15.0727 10.4533C16.33 9.60306 17.3439 8.45254 18.0182 7.11111C17.2833 5.64432 16.1423 4.40856 14.7249 3.54431C13.3074 2.68007 11.6704 2.22201 10 2.22222C9.00909 2.22222 8.03636 2.38222 7.12727 2.66667L5.72727 1.30667C7.03636 0.755555 8.48182 0.444444 10 0.444444ZM1.98182 7.11111C2.71668 8.5779 3.85769 9.81366 5.27512 10.6779C6.69256 11.5422 8.32956 12.0002 10 12C10.6273 12 11.2455 11.9378 11.8182 11.8133L9.74545 9.77778C9.11287 9.71148 8.52257 9.43544 8.0727 8.99558C7.62284 8.55571 7.34053 7.97852 7.27273 7.36L4.18182 4.32889C3.28182 5.08444 2.52727 6.02667 1.98182 7.11111Z" fill="#838383"></path>
                                            </svg>
                                        </button>
                                    </div>
                                </div>

                                <div class="mt-4 login-input">
                                    <svg class="start-icon" xmlns="http://www.w3.org/2000/svg" width="14" height="16" viewBox="0 0 14 16" fill="none">
                                        <path d="M7 12.1905C7.46413 12.1905 7.90925 12.0299 8.23744 11.7442C8.56563 11.4584 8.75 11.0708 8.75 10.6667C8.75 10.2625 8.56563 9.87494 8.23744 9.58917C7.90925 9.3034 7.46413 9.14286 7 9.14286C6.53587 9.14286 6.09075 9.3034 5.76256 9.58917C5.43437 9.87494 5.25 10.2625 5.25 10.6667C5.25 11.0708 5.43437 11.4584 5.76256 11.7442C6.09075 12.0299 6.53587 12.1905 7 12.1905ZM12.25 5.33333C12.7141 5.33333 13.1592 5.49388 13.4874 5.77965C13.8156 6.06542 14 6.453 14 6.85714V14.4762C14 14.8803 13.8156 15.2679 13.4874 15.5537C13.1592 15.8395 12.7141 16 12.25 16H1.75C1.28587 16 0.840752 15.8395 0.512563 15.5537C0.184374 15.2679 0 14.8803 0 14.4762V6.85714C0 6.453 0.184374 6.06542 0.512563 5.77965C0.840752 5.49388 1.28587 5.33333 1.75 5.33333H2.625V3.80952C2.625 2.79918 3.08594 1.83021 3.90641 1.11578C4.72688 0.401359 5.83968 0 7 0C7.57453 0 8.14344 0.0985363 8.67424 0.289983C9.20504 0.481429 9.68734 0.762037 10.0936 1.11578C10.4998 1.46953 10.8221 1.88949 11.042 2.35168C11.2618 2.81387 11.375 3.30925 11.375 3.80952V5.33333H12.25ZM7 1.52381C6.30381 1.52381 5.63613 1.76463 5.14384 2.19328C4.65156 2.62193 4.375 3.20331 4.375 3.80952V5.33333H9.625V3.80952C9.625 3.20331 9.34844 2.62193 8.85616 2.19328C8.36387 1.76463 7.69619 1.52381 7 1.52381Z" fill="#838383"></path>
                                    </svg>
                                    <div class="password-wrapper h-100">
                                        <input type="password" name="newPasswordConfirm" id="newPasswordConfirm" placeholder="Confirm New Password" class="password-input" required minlength="6" maxlength="20" title="Confirm Password should be at least 6 characters long">
                                        <button class="end-icon" id="eye2" type="button" onclick="togglePasswordVisibility('newPasswordConfirm', 'eye2')">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="16" viewBox="0 0 20 16" fill="none">
                                                <path d="M0.909091 1.12889L2.07273 0L17.2727 14.8622L16.1182 16L13.3182 13.2622C12.2727 13.6 11.1636 13.7778 10 13.7778C5.45455 13.7778 1.57273 11.0133 0 7.11111C0.627273 5.54667 1.62727 4.16889 2.9 3.07556L0.909091 1.12889ZM10 4.44444C10.7233 4.44444 11.417 4.7254 11.9285 5.22549C12.4399 5.72559 12.7273 6.40387 12.7273 7.11111C12.7277 7.41384 12.6755 7.71443 12.5727 8L9.09091 4.59556C9.38297 4.4951 9.69039 4.444 10 4.44444ZM10 0.444444C14.5455 0.444444 18.4273 3.20889 20 7.11111C19.2576 8.95372 17.9969 10.5532 16.3636 11.7244L15.0727 10.4533C16.33 9.60306 17.3439 8.45254 18.0182 7.11111C17.2833 5.64432 16.1423 4.40856 14.7249 3.54431C13.3074 2.68007 11.6704 2.22201 10 2.22222C9.00909 2.22222 8.03636 2.38222 7.12727 2.66667L5.72727 1.30667C7.03636 0.755555 8.48182 0.444444 10 0.444444ZM1.98182 7.11111C2.71668 8.5779 3.85769 9.81366 5.27512 10.6779C6.69256 11.5422 8.32956 12.0002 10 12C10.6273 12 11.2455 11.9378 11.8182 11.8133L9.74545 9.77778C9.11287 9.71148 8.52257 9.43544 8.0727 8.99558C7.62284 8.55571 7.34053 7.97852 7.27273 7.36L4.18182 4.32889C3.28182 5.08444 2.52727 6.02667 1.98182 7.11111Z" fill="#838383"></path>
                                            </svg>
                                        </button>
                                    </div>
                                </div>`;

function validateForm() {
    // Validate password match
    var password = document.getElementById("newPassword").value;
    var confirmPassword = document.getElementById("newPasswordConfirm").value;

    if (password !== confirmPassword) {
        printMessage("Confirm password is not matching",true);
        return false;
    }

    // If everything is valid, form can be submitted
    return true;
}


let otpRef = ''; // Variable to store the OTP reference
$('#forgotPassword').on('submit', function (e) {
    e.preventDefault();
});

// Step 1: Send email to the server for OTP generation
$('#verifyEmail').on('click', function (e) {
    const email = $('#username').val();

    if (email) {
        printMessage("Verifying Email. Please wait...",false);
        $('#username').prop('readonly', true);
        $.ajax({
            url: '/api/v1/auth/forgot-password', // Replace with your server endpoint
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                email: email
            }),
            success: function (response) {
                $("#addBlocks").html(otpBlock);
                $("#btnDiv").html(otpBtn);
                 printMessage("An OTP code was sent to your email address.",false);
            },
            error: function () {
                printMessage('Error generating OTP.',true);
            }
        });
    } else {
        printMessage('Please enter a valid email.',true);
    }
});

$(document).on('click', '#verifyOTP', function () {
    const otpCode = $('#OtpCode').val();
    email = $('#username').val();
    // Step 2: Send OTP to the server for validation
    $.ajax({
        url: '/api/v1/auth/verify-otp-code', // Replace with your server endpoint
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            email: email,
            otpCode: otpCode
        }),
        success: function (validationResponse) {
            console.log("Status: " + validationResponse.status);
            if (validationResponse.status === true) {
                otpRef = validationResponse.data.ref;  // Store the OTP reference
                // Step 3: OTP validated, ask for new password
                $('#OtpCode').prop('readonly', true);
                $("#addBlocks").html(newPasswordBlock);
                $("#btnDiv").html(newPasswordBtn);
                printMessage("The OTP code is correct. Please enter a new password.",false);
            } else {
                printMessage('Invalid OTP. Please try again.',true);
            }
        },
        error: function () {
            printMessage('Incorrect OTP code. Please try again',true);
        }
    });
});

$(document).on('click', '#submitButton', function () {
    const newPassword = $('#newPassword').val();
    const newPasswordConfirm = $('#newPasswordConfirm').val();

    if (newPassword && otpRef && validateForm()) {
        // Step 4: Send new password to the server to update
        $.ajax({
            url: '/api/v1/auth/set-password', // Replace with your server endpoint
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({
                ref: otpRef,
                password: newPassword,
                confirmPassword: newPasswordConfirm
            }),
            success: function (updateResponse) {
                // Step 5: Print server response message
                printMessage(updateResponse.message,false);
                if (updateResponse.status === true) {
                    $('#forgotPassword').trigger("reset");
                }
            },
            error: function () {
                printMessage('Error updating password.',true);
            }
        });
    }
});

function printMessage(message, error) {
    var style = error ? "color: red" : "color: green";
    $("#message").html("<p class='pt-3' style='" + style + "'>" + message + "</p>");

    // If it's an error message, hide it after 4 seconds
    if (error) {
        setTimeout(function() {
            $("#message").html(""); // Clear the message after 4 seconds
        }, 3000);
    }
}

// Function to allow only numeric input
function restrictNumericInput(selector, maxLength) {
    $(selector).on("input", function() {
        // Remove any non-numeric character
        this.value = this.value.replace(/[^0-9]/g, '');

        // Ensure length doesn't exceed maxLength
        if (this.value.length > maxLength) {
            this.value = this.value.substring(0, maxLength);
        }
    });
}
