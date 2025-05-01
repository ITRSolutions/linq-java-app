var tableData = null;
    var currentPage = 1;  // Start on the first page
    var pageSize = 10;    // Number of records per page
    var totalElements = 15; // Total number of elements (can be fetched from your API response)
    var totalPages = Math.ceil(totalElements / pageSize); // Calculate total pages

    function updatePagination(page) {
        // Fetch data for the current page
        $.ajax({
            url: `/api/v1/forms?pageName=${pageName}&page=${page - 1}`,
            method: 'GET',
            success: function(response) {
                if (response.status) {
                    let data = response.data.content;
                    tableData = data;
                    let tableBody = $('table tbody');
                    tableBody.empty(); // Clear existing rows

                    displayTableData(data, page);

                    // Update pagination text

//                    if(tableData.length > 9)
                    {
//                        $('#pagination').show();
//                        console.log(response.data);
                        totalElements = response.data.totalElements;
                        let start = (page - 1) * pageSize + 1;
                        let end = Math.min(page * pageSize, totalElements);
                        totalPages = Math.ceil(totalElements / pageSize);
                        $('#paginationText').text(`Showing ${start}-${end} of ${totalElements}`);

                                        // Update page controls
                        $('#prevPage').prop('disabled', page <= 1);
                        $('#nextPage').prop('disabled', page >= totalPages);
                    }
                }
            },
            error: function() {
                alert('Error fetching data');
            }
        });
    }

    function displayTableData(data, page) {
                    // Loop through the fetched data and display rows with dynamic row number
                    data.forEach((item, index) => {
                        let rowNumber = (page - 1) * pageSize + index + 1; // Row number from 1
                         let updatedBy = item.updatedBy ? `${item.updatedBy.firstName} ${item.updatedBy.lastName}` : "N/A";
                        let row = tableColumn(item,rowNumber,updatedBy);
                        $('.table tbody').append(row);
                    });
    }

        function tableColumn(item,rowNumber,updatedBy) {
                let followBack = item.followBack ? `${item.followBack}` : "N/A";
                return `<tr>
                                <td>${rowNumber}</td> <!-- Display row number from 1 to N -->
                                <td>${item.firstName}</td>
                                <td>${item.lastName}</td>
                                <td>${item.phoneNumber}</td>
                                <td>${item.email}</td>
                                <td> ${formatDate(item.createdAt)}</td>
                                <td>${followBack}</td>
                                <td>
                                    <i class="fa fa-edit text-success text-active updateBtn" data-id="${item.id}"></i>
                                </td>
                            </tr>`;
        }

    // Next page button
    $('#nextPage').click(function() {
        if (currentPage < totalPages) {
            currentPage++;
            updatePagination(currentPage);
        }
    });

    // Previous page button
    $('#prevPage').click(function() {
        if (currentPage > 1) {
            currentPage--;
            updatePagination(currentPage);
        }
    });


$(document).on('click', '.updateBtn', function () {
    // Get the closest row to the clicked button
    let rowIndex = $(this).closest('tr').index();

    // Assuming you have a global `tableData` array that holds all your data
    let selected = tableData[rowIndex];
    let updatedBy = selected.updatedBy ? `${selected.updatedBy.firstName} ${selected.updatedBy.lastName}`: 'NA';

    // Populate the modal fields with the data from the selected row
//    $('#viewPatientData').find('.modal-title').text(`Refer friend: ${selected.firstName} ${selected.lastName}`);

    // Fill the fields inside the modal with the selected data
    viewClientData(selected,updatedBy);

    // Show the modal
    $('#viewPatientData').modal('show');
});

    // Event listener for keypress in the search input
$('input[name="search"]').on('keyup', function() {
    var searchString = $(this).val().trim();

    if (searchString) {
        // If the search string is not empty, make the API call
        $.ajax({
            url: '/api/v1/forms/search', // API URL
            type: 'GET',
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("X-CSRF-TOKEN", $("#_csrf").val());
                },
            data: {
                searchString: searchString, // Search query parameter
                pageName: pageName   // Page name parameter (fixed for your case)
            },
            success: function(response) {
                // Assuming the response has the data in response.data.content
                if (response && response.data && response.data) {
                    var rows = '';
                    var rowNumber = 1; // Initialize rowNumber starting from 1
                    tableData = response.data;

                    // Iterate over the fetched data and populate the table rows
                    $.each(response.data, function(index, item) {
                        let updatedBy = item.updatedBy ? `${item.updatedBy.firstName} ${item.updatedBy.lastName}` : "N/A";
                        rows += tableColumn(item,rowNumber,updatedBy);
                        rowNumber++; // Increment the row number after each iteration
                    });

                    // Update the table body with new rows
                    $('.table tbody').html(rows);
                }
            },
            error: function(xhr, status, error) {
                console.error('Error fetching data:', error);
            }
        });
    } else {
        updatePagination(currentPage);
    }});

$(document).ready(function() {
    $('#viewPatientData form').submit(function(e) {
        e.preventDefault(); // Prevent the default form submission

        // Get the updated data from the modal
        var rowId = $('#rowId').val(); // Get the rowId
        var followBack = $('#followBack').val(); // Get selected followBack status
        var comment = $('#comment').val(); // Get comment value

        // Send the PUT request with the updated data
        $.ajax({
            url: `/api/v1/forms/${rowId}`, // API URL with dynamic rowId
            type: 'PUT',
            contentType: "application/json",
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("X-CSRF-TOKEN", $("#_csrf").val());
                },
            data: JSON.stringify({
                followBack: followBack,
                comment: comment
            }),
            success: function(response) {
                if (response.status) {
                    updatePagination(currentPage);
                    alert(response.message); // Show the success message from the API
                    $('#viewPatientData').modal('hide'); // Hide the modal
                } else {
                    // Show an error message if something goes wrong
                    alert('Error updating data');
                }
            },
            error: function(xhr, status, error) {
                console.error('Error fetching data:', error);
                alert('An error occurred while updating data.');
            }
        });
    });
});

    function formatDate(dateString) {
        var date = new Date(dateString);
        return date.toLocaleString();
    }
