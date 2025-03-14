
$('#addPageFunction').click(function () {
    $('#addPage').modal('show');
});

$('.updatePage').click(function () {
    $('#updatePage').modal('show');
});

$('.deletePage').click(function () {
    if (confirm("Are you sure to delete this page!")) {


    }
});


$(document).ready(function() {
    let currentPage = 1; // Default starting page
    let totalPages = 1;  // Total pages, will be set based on response data

    // Function to load and display page data
    function loadPage(page) {
        $.ajax({
            url: `/api/v1/web_page?page=${page}`, // Your API endpoint here
            method: 'GET',
            success: function(response) {
                if (response.status && response.data) {
                    const data = response.data.data;  // Array of pages
                    const total = response.data.total;  // Total number of pages

                    // Update page info (showing X of Y)
                    $('#page-info').text(`${data.length} of ${total}`);

                    // Clear existing rows
                    $('#dynamic-table-body').empty();

                    // Add new rows to the table
                    $.each(data, function(index, page) {
                        const row = `
                            <tr>
                                <td>${page.id}</td>
                                <td class="hidden-xs">
                                    <a href="#" onclick="return false;" class="openContentBlock">
                                        ${page.slug}
                                    </a>
                                </td>
                                <td>${page.title}</td>
                                <td>${page.status}</td>
                                <td>${formatDate(page.updatedAt)}</td>
                                <td>${page.createdBy ? page.createdBy.name : 'Admin'}</td>
                                <td>${page.updatedBy ? page.updatedBy.name : 'Admin'}</td>
                                <td>
                                    <i class="fa fa-edit text-success text-active updatePage"></i> <br>
                                    <i class="fa fa-times text-danger text deletePage"></i>
                                </td>
                            </tr>
                        `;
                        $('#dynamic-table-body').append(row);
                    });

                    // Calculate total pages for pagination
                    totalPages = Math.ceil(total / 10); // Assuming 10 items per page

                    // Enable/Disable pagination buttons
                    $('#prevPage').prop('disabled', currentPage <= 1);
                    $('#nextPage').prop('disabled', currentPage >= totalPages);
                }
            },
            error: function(xhr, status, error) {
                console.error('Error fetching data:', error);
            }
        });
    }

    // Function to format the date to a readable format
    function formatDate(dateString) {
        const date = new Date(dateString);
        const day = ("0" + date.getDate()).slice(-2);
        const month = ("0" + (date.getMonth() + 1)).slice(-2); // Months are zero-based
        const year = date.getFullYear();
        return `${day}-${month}-${year}`;
    }

    // Handle 'Prev' button click
    $('#prevPage').click(function() {
        if (currentPage > 1) {
            currentPage--;
            loadPage(currentPage);
        }
    });

    // Handle 'Next' button click
    $('#nextPage').click(function() {
        if (currentPage < totalPages) {
            currentPage++;
            loadPage(currentPage);
        }
    });

    // Initially load the first page
    loadPage(currentPage);
});
