var pageName = "high-cholesterol";

function viewClientData(selected,updatedBy) {
    $('#viewPatientData').find('#rowId').val(selected.id);
    $('#viewPatientData').find('.first-name').text(selected.firstName);
    $('#viewPatientData').find('.last-name').text(selected.lastName);
    $('#viewPatientData').find('.phone-number').text(selected.phoneNumber);
    $('#viewPatientData').find('.email').text(selected.email);
    $('#viewPatientData').find('.dob').text(selected.dateOfBirth);

    $('#viewPatientData').find('.zip-code').text(selected.zipCode);
    $('#viewPatientData').find('.best-time-contact').text(selected.bestContactTime);
    $('#viewPatientData').find('.state').text(selected.state);
    $('#viewPatientData').find('.created-at').text(formatDate(selected.createdAt));
    $('#viewPatientData').find('.updated-at').text(formatDate(selected.updatedAt));
    $('#viewPatientData').find('.updated-by').text(updatedBy);
    $('#followBack').val(selected.followBack);
    $('#comment').val(selected.comment);
}

$(document).ready(function() {

    // URL to fetch disease list
    const apiUrl = "/api/v1/forms/diseases";
    // Fetch the disease data from the API
    $.get(apiUrl, function(response) {
        // Check if the response is valid and contains data
        if (response.status === true && Array.isArray(response.data)) {
            const diseases = response.data;

            // Loop through each disease
            diseases.forEach(function(disease) {
                // Format the disease name (replace spaces with dash, remove quotes)
                let formattedDisease = disease
                    .replace(/\s*\([^)]*\)/g, '')      // Remove parentheses and content inside
                    .toLowerCase()                     // Convert to lowercase
                    .replace(/ /g, '-')                // Replace spaces with dash
                    .replace(/'/g, '');                // Remove single quotes

                // Append the formatted disease to the select box
                $('#diseaseSelect').append(
                    `<option value="${formattedDisease}">${disease}</option>`
                );
            });
        } else {
            console.log("Failed to fetch or invalid data structure.");
        }
    }).fail(function() {
        console.log("Error fetching disease data.");
    });


        $('#diseaseSelect').change(function() {
            var selectedValue = $(this).val();
            if (selectedValue) {
                console.log('Selected disease:', selectedValue);
                pageName = selectedValue;
                updatePagination(currentPage);
//                $("#header").html("Appointment Lead: "+pageName);
            } else {
                console.log('No disease selected');
            }
        });
});

