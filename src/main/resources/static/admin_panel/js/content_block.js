var PageIdCB = 0;
var contentBlockTable = null;
var pageNameCB = null;

$(document).on('click', '.openContentBlock', function () {
    $("#content_blocks-tab").click();
        $("#pageIdCB").val(0);
        let rowIndex = $(this).closest('tr').index();
        let selectedPage = pageTableData[rowIndex];
        pageNameCB = $(this).closest('td');
        pageNameCB = pageNameCB[0].children[0].textContent
        fetchContentBlocks(selectedPage.id);
        $("#pageIdCB").val(selectedPage.id);
        PageIdCB = selectedPage.id;
});

$(document).on('click', '#addContentBlockFunction', function () {
    if(!pageNameCB) {
        alert("No page is linked.");
        $("#Pages-tab").click();
        return 0;
    }

    pageNameCB ? $('#addContentBlock').modal('show') : 0;
    $('#addContentBlock').find('[name="pageName"]').val(pageNameCB);

        var table = $("#contentBlockTable tbody tr");
        if(!table[0].innerText.includes("No content blocks found")) {
            var index = parseInt($(table[table.length-1]).find("td")[0].textContent);
            $("#orderIndex").val(index + 1);
        } else {
            $("#orderIndex").val(1);
        }
});

$(document).on('click', '.updateContentBlock', function () {
    let rowIndex = $(this).closest('tr').index();
    let selectedCB = contentBlockTable[rowIndex];

    let pageName = $("#contentBlockTable").find("td")[2].textContent;
    $("input[name='updatePageName']").val(pageName);
    $("input[name='updateTitleHeader']").val(selectedCB.content);
    $("input[name='updateOrderIndex']").val(selectedCB.orderIndex);
    $("#CBrowId").val(selectedCB.id);

    // Show the modal
    $('#updateContentBlock').modal('show');
});


$('.deleteContentBlock').click(function () {
    if (confirm("Are you sure to delete this page!")) {

    }
});

//<------------fetchContentBlocks Start------------->
    function fetchContentBlocks(id) {
      $.ajax({
        url: "/api/v1/content_block/"+id,
        type: "GET",
        success: function (response) {
          if (response.status && response.data.length > 0) {
            renderTableCB(response.data);
            contentBlockTable = response.data;
          } else {
            $("#contentBlockTable tbody").html("<tr><td colspan='6' class='text-center'>No content blocks found</td></tr>");
          }
        },
        error: function () {
          alert("Error fetching content blocks.");
        }
      });
    }

    function renderTableCB(data) {
      let rows = "";
      data.forEach((item) => {
        let updatedBy = item.updatedBy ? `${item.updatedBy.firstName} ${item.updatedBy.lastName}` : "N/A";
        let updatedAt = item.updatedAt ? new Date(item.updatedAt).toLocaleString() : "N/A";

        rows += `
          <tr>
            <td>${item.orderIndex}</td>
            <td class="hidden-xs">
              <a href="#" onclick="return false;" class="openSlide">${truncateCBText(item.content, 50)}</a>
            </td>
            <td>${item.page.slug}</td>
            <td>${updatedAt}</td>
            <td>${updatedBy}</td>
            <td>
              <i class="fa fa-edit text-success text-active updateContentBlock"></i> <br>
              <i class="fa fa-times text-danger text deleteContentBlock" style="display:none"></i>
            </td>
          </tr>`;
      });

      $("#contentBlockTable tbody").html(rows);
    }

    function truncateCBText(text, length) {
        return text.length > length ? text.substring(0, length) + '...' : text;
    }
//<------------fetchContentBlocks End------------->

//<------------Add ContentBlocks Start------------->
  $(document).ready(function () {
    $("#addContentBlock form").submit(function (event) {
      event.preventDefault(); // Prevent default form submission

      let pageId = $("#pageIdCB").val(); // Get page ID from a hidden attribute
      let content = $("input[name='titleHeader']").val().trim();
      let orderIndex = $("input[name='orderIndex']").val().trim();

      // Basic Validation
      if (!pageId || !content || !orderIndex) {
        alert("All fields are required!");
        return;
      }

      let requestData = {
        orderIndex: parseInt(orderIndex),
        pageId: parseInt(pageId),
        content: content
      };

      $.ajax({
        url: "/api/v1/content_block",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(requestData),
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("X-CSRF-TOKEN", $("#_csrf").val());
                },
        success: function (response) {
          if (response.status) {
            fetchContentBlocks(PageIdCB);
            alert("Content Block created successfully!");
            $("#addContentBlock").modal("hide"); // Close modal on success
            $("#addContentBlock form")[0].reset(); // Reset form fields
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
              alert("Error: Unable to create Content Block.");
            }
          }
      });
    });
      //<------------Add ContentBlocks End------------->

//<------------Update ContentBlocks Start------------->
        $("#updateContentBlock form").submit(function (event) {
          event.preventDefault(); // Prevent default form submission

          let CBId = $("#CBrowId").val();
          let content = $("input[name='updateTitleHeader']").val().trim();
          let orderIndex = $("input[name='updateOrderIndex']").val().trim();

          // Basic Validation
          if (!CBId || !content || !orderIndex) {
            alert("All fields are required!");
            return;
          }

          let requestData = {
            orderIndex: parseInt(orderIndex),
            pageId: parseInt(CBId),
            content: content
          };

          $.ajax({
            url: "/api/v1/content_block/"+CBId, // Update Content Block API
            type: "PUT", // Send POST request
            contentType: "application/json",
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("X-CSRF-TOKEN", $("#_csrf").val());
                },
            data: JSON.stringify(requestData),
            success: function (response) {
              if (response.status) {
                fetchContentBlocks(PageIdCB);
                alert("Content Block updated successfully!");
                $("#updateContentBlock").modal("hide"); // Close modal on success
                $("#updateContentBlock form")[0].reset(); // Reset form fields
              } else {
                alert("Error: " + response.message);
              }
            },
            error: function (xhr) {
              let response = xhr.responseJSON;
              if (response && response.errors && response.errors.content) {
                alert("Validation Error: " + response.errors.content[0]);
              } else {
                alert("Error: Unable to update Content Block.");
              }
            }
          });
        });
        //<------------Update ContentBlocks End------------->

  });