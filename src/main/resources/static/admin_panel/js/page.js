var pageTableData = null;
    let currentPage = 0; // Start from page 0 (as per API)
    let totalRecords = 0;
    let pageSize = 10; // Backend handles the page size
    let totalPages = 0;

$('#addPageFunction').click(function () {
    $('#addPage').modal('show');
});

//<------------Update Webpage------------->
$(document).on('click', '.updatePage', function () {
    // Get the index or ID of the page associated with this row
    var rowIndex = $(this).closest('tr').index();
    console.log("Update form index: "+rowIndex);

    // Retrieve the data of the selected row from the pageTableData array
    var selectedPage = pageTableData[rowIndex];



    // Populate the modal fields with the data from selectedPage
    $('#updatePage').find('[name="updateSlug"]').val(selectedPage.slug);
    $('#updatePage').find('[name="updateStatus"]').val(selectedPage.status);
    $('#updatePage').find('[name="updateTitle"]').val(selectedPage.title);
    $('#updatePage').find('[name="rowId"]').val(selectedPage.id);

    // Show the modal
    $('#updatePage').modal('show');
});
//<------------Update Webpage------------->

$(document).on('click', '.deletePage', function () {
    if (confirm("Are you sure to delete this page!")) {


    }
});


            $(document).ready(function() {

//            <------------Display Webpages name in Select box start------------->
                // Perform AJAX call to fetch data
                $.ajax({
                    url: '/api/v1/web_page/getallnames', // Your endpoint URL
                    method: 'GET',
                    dataType: 'json', // Expect JSON response
                    success: function(response) {
                        // Check if status is true
                        if (response.status) {
                            // Get the array of files from the response
                            var files = response.data;

                            // Loop through each file and add it as an option to the select box
                            $.each(files, function(index, file) {
                                $('.webPageSelect').append('<option value="' + file + '">' + file + '</option>');
                            });
                        } else {
                            alert(response.message); // Show error message if status is false
                        }
                    },
                    error: function(xhr, status, error) {
                        console.error("Error fetching data: " + error);
                    }
                });
//<------------Display Webpages name in Select box End------------->

//<------------Display data in Table Start------------->

    function fetchWebPages(page) {
      $.ajax({
        url: `/api/v1/web_page?page=${page}`, // Fetch paginated data from the server
        type: "GET",
        success: function (response) {
          if (response.status && response.data.data.length > 0) {
            totalRecords = response.data.total;
            totalPages = Math.ceil(totalRecords / pageSize);
            pageTableData = response.data.data;
            renderTable(response.data.data);
            updatePaginationUI(page, totalRecords);
          } else {
            $("#webPageTable tbody").html("<tr><td colspan='7' class='text-center'>No data available</td></tr>");
          }
        },
        error: function () {
          alert("Error fetching data");
        }
      });
    }

    function renderTable(data) {
      let rows = "";
      data.forEach((item) => {
        rows += `
          <tr>
            <td>${item.id}</td>
            <td class="hidden-xs">
              <a href="#" onclick="return false;" class="openContentBlock">${item.slug}</a>
            </td>
            <td>${item.title}</td>
            <td>${item.status}</td>
            <td>${new Date(item.updatedAt).toLocaleString()}</td>
            <td>${item.updatedBy.firstName} ${item.updatedBy.lastName}</td>
            <td>
              <i class="fa fa-edit text-success text-active updatePage"></i>
              <br> <i class="fa fa-times text-danger text deletePage" style="display:none"></i>
            </td>
          </tr>`;
      });

      $("#webPageTable tbody").html(rows);
    }

    function updatePaginationUI(page, totalRecords) {
      let startRecord = page * pageSize + 1;
      let endRecord = Math.min((page + 1) * pageSize, totalRecords);

      $(".text-muted.m-r-sm").text(`Showing ${startRecord}-${endRecord} of ${totalRecords}`);

      // Enable/disable pagination buttons
      $(".btn-group .fa-angle-left").parent().toggleClass("disabled", page === 0);
      $(".btn-group .fa-angle-right").parent().toggleClass("disabled", page >= totalPages - 1);
    }

    // Handle Previous Button Click
    $(".btn-group .fa-angle-left").parent().click(function () {
      if (currentPage > 0) {
        currentPage--;
        fetchWebPages(currentPage);
      }
    });

    // Handle Next Button Click
    $(".btn-group .fa-angle-right").parent().click(function () {
      if (currentPage < totalPages - 1) {
        currentPage++;
        fetchWebPages(currentPage);
      }
    });

    // Initial Fetch
    fetchWebPages(currentPage);

    $("#page_reload").click(function (event) {
        location.reload();
    });

//<------------Display data in Table End------------->

  //<------------Slug search start------------->
    $("#pageSearchInput").click(function () {

        $(this).on("keyup", function(event) {
      let searchQuery = $("#pageSearchInput").val().trim();

      // Check if input is empty
      if (!searchQuery) {
         fetchWebPages(0);
        return;
      }

      let requestData = { slug: searchQuery };

      $.ajax({
        url: "/api/v1/web_page/search",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(requestData),
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("X-CSRF-TOKEN", $("#_csrf").val());
                },
        success: function (response) {
          if (response.status && response.data.length > 0) {
            pageTableData = response.data;
            renderTable(response.data);
          } else {
            $("#webPageTable tbody").html("<tr><td colspan='7' class='text-center'>No results found</td></tr>");
          }
        },
        error: function () {
          alert("Error: Unable to fetch search results.");
        }
      });
        });
    });
  //<------------Slug search end------------->

//<------------Add Webpage------------->
    $("#addWebPage").click(function (event) {
      event.preventDefault(); // Prevent default button action

      let title = $("input[name='title']").val().trim();
      let slug = $("select[name='slug']").val();
      let status = $("select[name='status']").val();

      // Basic Validation
      if (!title || !slug || !status) {
        alert("All fields are required!");
        return;
      }

      let requestData = {
        title: title,
        slug: slug,
        status: status
      };

      $.ajax({
        url: "/api/v1/web_page",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(requestData),
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("X-CSRF-TOKEN", $("#_csrf").val());
                },
        success: function (response) {
          if (response.status) {
            fetchWebPages(currentPage);
            alert("Web-page created successfully!");
            $("#addPage").modal("hide"); // Close modal on success
            $("#addPage form")[0].reset();
          } else {
            alert("Error: " + response.message);
          }
        },
  error: function(xhr, status, error) {
    // Try to parse the response as JSON
    try {
      var response = JSON.parse(xhr.responseText);
      // Check if the message property exists and display it in the alert
      if (response.message) {
        alert("Error: " + response.message);
      } else {
        alert("An unknown error occurred.");
      }
    } catch (e) {
      // If JSON parsing fails, display a generic error
      alert("Error: Unable to parse the response.");
    }
  }
      });
    });
//<------------Add Webpage End------------->

//<------------updatePage Webpage Start------------->
$("#updatePage form").submit(function (event) {
      event.preventDefault(); // Prevent default form submission

      let title = $("input[name='updateTitle']").val().trim();
      let slug = $("select[name='updateSlug']").val();
      let status = $("select[name='updateStatus']").val();
      let rowIndex = $("input[name='rowId']").val();

      // Basic Validation
      if (!title || !slug || !status) {
        alert("All fields are required!");
        return;
      }

      let requestData = {
        title: title,
        slug: slug,
        status: status
      };

      $.ajax({
        url: "/api/v1/web_page/"+rowIndex, // API endpoint for updating the web page
        type: "PUT", // Use PUT for updates
        contentType: "application/json",
        data: JSON.stringify(requestData),
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("X-CSRF-TOKEN", $("#_csrf").val());
                },
        success: function (response) {
          if (response.status) {
            fetchWebPages(currentPage);
            alert("Web-page updated successfully!");
            $("#updatePage").modal("hide");
            $("#updatePage form")[0].reset();
          } else {
            alert("Error: " + response.message);
          }
        },
  error: function(xhr, status, error) {
    // Try to parse the response as JSON
    try {
      var response = JSON.parse(xhr.responseText);
      // Check if the message property exists and display it in the alert
      if (response.message) {
        alert("Error: " + response.message);
      } else {
        alert("An unknown error occurred.");
      }
    } catch (e) {
      // If JSON parsing fails, display a generic error
      alert("Error: Unable to parse the response.");
    }
  }
      });
    });
//<------------updatePage Webpage End------------->


              });