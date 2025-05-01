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

    var tableData = null;

    // URL to fetch disease list
    const apiUrl = "/api/v1/forms/get-job-role";
    // Fetch the disease data from the API
    $.get(apiUrl, function(response) {
        // Check if the response is valid and contains data
        if (response.status === true && Array.isArray(response.data)) {
            const jobApplications = response.data;

            // Loop through each disease
            jobApplications.forEach(function(jobApplication) {
                // Format the disease name (replace spaces with dash, remove quotes)
let formattedJobTitle = jobApplication
    .replace(/\s*\([^)]*\)/g, '')         // Remove parentheses and content inside
    .replace(/[^\w\s-]/g, '')             // Remove special characters except dash and space
    .trim()                               // Trim leading/trailing whitespace
    .toLowerCase()                        // Convert to lowercase
    .replace(/\s+/g, '-')                 // Replace spaces with single dash
    .replace(/-+/g, '-');                 // Collapse multiple dashes

                // Append the formatted disease to the select box
                $('#applicationSelect').append(
                    `<option value="${formattedJobTitle}">${jobApplication}</option>`
                );
            });
        } else {
            console.log("Failed to fetch or invalid data structure.");
        }
    }).fail(function() {
        console.log("Error fetching disease data.");
    });

        let currentPage = 0;
        const pageSize = 10;
        var pageName = null; // Replace dynamically if needed

        $('#applicationSelect').change(function() {
            var selectedValue = $(this).val();
            if (selectedValue) {
                console.log('Selected disease:', selectedValue);
                pageName = selectedValue;
                loadApplications();
            } else {
                console.log('No Role selected');
            }
        });

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: 'numeric',
        minute: '2-digit',
        hour12: true
    });
}


        function loadApplications() {
            $.ajax({
                url: '/api/v1/forms/get-job-applications',
                method: 'GET',
                data: {
                    pageName: pageName,
                    page: currentPage
                },
                success: function (res) {
                    if (!res.status || !res.data) {
                        $('#applicationsBody').html('<tr><td colspan="8" class="text-center">No data found.</td></tr>');
                        $('#paginationText').text(`Showing 0 of 0`);
                        return;
                    }

                    const data = res.data;
                    const applications = data.content;
                    const totalElements = data.totalElements;

                    const $tbody = $('#applicationsBody');
                    $tbody.empty();

                    if (applications.length === 0) {
                        $tbody.append('<tr><td colspan="8" class="text-center">No job applications found.</td></tr>');
                        $('#paginationText').text(`Showing 0 of 0`);
                        return;
                    }
                    tableData = applications;
//                    let rowNumber = currentPage * pageSize + 1;
                    loadTableData(applications, $tbody);

                    const start = currentPage * pageSize + 1;
                    const end = start + applications.length - 1;
                    $('#paginationText').text(`Showing ${start}-${end} of ${totalElements}`);

                    // Enable/disable prev/next based on position
                    $('#prevPage').prop('disabled', data.first);
                    $('#nextPage').prop('disabled', data.last);
                },
                error: function (err) {
                    console.error("Error fetching applications:", err);
                    $('#applicationsBody').html('<tr><td colspan="8" class="text-center">Failed to load data.</td></tr>');
                }
            });
        }

        function loadTableData(applications, $tbody) {
                            applications.forEach(item => {
                                const row = `<tr>
                                    <td>${item.id}</td>
                                    <td>${item.name}</td>
                                    <td>${item.email}</td>
                                    <td>${item.phone}</td>
                                    <td>${item.jobTitle.trim()}</td>
                                    <td>${formatDate(item.createdAt)}</td>
                                    <td>
                                        <i class="fa fa-file text-success text-active viewCV"
                                           onclick="window.open('${item.resumeURL}', '_blank');"></i>
                                    </td>
                                    <td>
                                        <i class="fa fa-edit text-success text-active updateBtn" data-id="${item.id}"></i>
                                    </td>
                                </tr>`;
                                $tbody.append(row);
                            });
        }

        // Pagination controls
        $('#prevPage').click(function () {
            if (currentPage > 0) {
                currentPage--;
                loadApplications();
            }
        });

        $('#nextPage').click(function () {
            currentPage++;
            loadApplications();
        });

let searchTimeout;

$('#searchInput').on('keyup', function () {
    clearTimeout(searchTimeout);

    searchTimeout = setTimeout(function () {
        const searchString = $('#searchInput').val().trim();

        $.ajax({
            url: `/api/v1/forms/search-application`,
            method: 'GET',
            data: { pageName, searchString },
            success: function (response) {
                const data = response.data;
                const $tbody = $('#applicationsBody');
                $tbody.empty();

                if (data.length === 0) {
                    $tbody.append('<tr><td colspan="6">No results found.</td></tr>');
                    return;
                }

                tableData = data;
                loadTableData(data, $tbody);
            },
            error: function (err) {
                console.error('Search failed:', err);
                alert('Error performing search.');
            }
        });

    }, 300); // 300ms delay after typing stops
});


        $(document).on('click', '.updateBtn', function() {
            let rowIndex = $(this).closest('tr').index();
            let data = tableData[rowIndex];

               $('#app-id').val(data.id);
               $('#app-name').text(data.name);
               $('#app-email').text(data.email);
               $('#app-phone').text(data.phone);
               $('#app-resume').attr('href', data.resumeURL);
               $('#app-cover').attr('href', data.coverURL || '#').text(data.coverURL ? 'Download' : 'N/A');
               $('#app-eligibility').text(data.eligibleToWorkInUSA ? 'Yes' : 'No');
               $('#app-visa').text(data.requiresVisaSponsorship ? 'Yes' : 'No');
               $('#app-reside').text(data.residesInUSA ? 'Yes' : 'No');
               $('#app-compensation').text(data.compensation);
               $('#app-title').text(data.jobTitle);
               $('#app-page').text(data.pageName);
               $('#app-created').text(formatDate(data.createdAt));

               $('#viewJobApplicationModal').modal('show');

            $('#updateSlideContent').modal('show');
        });

});

