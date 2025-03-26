
  var userTable = null;

 // Initialize page variable for pagination
let currentPage = 0;
let totalUsers = 0;

    $(document).on('click', '.updateUserBtn', function () {
        let rowIndex = $(this).closest('tr').index();
        let selected = userTable[rowIndex];

        $('#firstName').val(selected.firstName);
        $('#lastName').val(selected.lastName);
        $('#contactNumber').val(selected.contactNumber);
        $('#dob').val(selected.dob);
        $('#gender').val(selected.gender);
        $('#city').val(selected.city);
        $('#state').val(selected.state);
        $('#zipCode').val(selected.zipCode);
        $('#country').val(selected.country);
        $('#activateUser').val(selected.activateUser);

        const userId = $(this).data('id');
        $('#updateUserRow').val(userId);

        $('#updateUser').modal('show');
    });

      $(document).ready(function() {

          $('#refresh').click(function(){
               loadUsers(currentPage);
          });


//<------------Display User Start------------->

        // Function to fetch and display users with pagination
        function loadUsers(page) {
          $.ajax({
            url: `http://localhost:8090/api/v1/users?page=${page}`,
            type: 'GET',
            success: function(response) {
              const users = response.data.data;
              totalUsers = response.data.total; // Get total number of users
              userTable = response.data.data;
              let rows = '';

              // Update pagination info: Showing X of Y
              const startIndex = page * 10 + 1; // Assuming 10 users per page
              const endIndex = Math.min((page + 1) * 10, totalUsers);
              $('#pagination-info').text(`Showing ${startIndex} of ${totalUsers}`);

              // Build the table rows
              rows = loadTableData(users);

              $('#user-table tbody').html(rows);
            }
          });
        }

        function loadTableData(users) {
              let rows = '';
              users.forEach(user => {
                rows += `
                  <tr>
                    <td>${user.id}</td>
                    <td>${user.firstName}</td>
                    <td>${user.lastName}</td>
                    <td>${user.email}</td>
                    <td>${formatDate(user.createdAt)}</td>
                    <td>${formatDate(user.updatedAt)}</td>
                    <td>${user.isEmailVerified ? 'Yes' : 'No'}</td>
                    <td>${user.updatedBy ? user.updatedBy  : 'NA'}</td>
                    <td>
                      <i class="fa fa-edit text-success text-active updateUserBtn" data-id="${user.id}"></i><br>
                      <i class="fa fa-times text-danger text deleteUser" data-id="${user.id}"></i>
                    </td>
                  </tr>
                `;
              });

              return rows;
        }

        // Load users initially
        loadUsers(currentPage);

        // Pagination buttons
        $('#prev-page').click(function() {
          if (currentPage > 0) {
            currentPage--;
            loadUsers(currentPage);
          }
        });

        $('#next-page').click(function() {
          if ((currentPage + 1) * 10 < totalUsers) {
            currentPage++;
            loadUsers(currentPage);
          }
        });
//<------------Display User End------------->

//<------------Update User Start------------->
    // Handle the modal form submission (Save button)
    $('#updateUser form').submit(function(e) {
        e.preventDefault();

        // Get the updated data from the form
        const updatedData = {
            firstName: $('#firstName').val(),
            lastName: $('#lastName').val(),
            contactNumber: $('#contactNumber').val(),
            dob: $('#dob').val(),
            gender: $('#gender').val(),
            city: $('#city').val(),
            state: $('#state').val(),
            zipCode: $('#zipCode').val(),
            country: $('#country').val()
            activateUser: $('#activateUser').val()
        };

        const userId = $('#updateUserRow').val();

        // Send the PUT request to update the user
        $.ajax({
            url: `http://localhost:8090/api/v1/users/${userId}`,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(updatedData),
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("X-CSRF-TOKEN", $("#_csrf").val());
                },
            success: function(response) {
                alert('User updated successfully!');
                loadUsers(currentPage);  // Reload users after updating
                $('#updateUser').modal('hide');  // Close the modal
            },
            error: function() {
                alert('Error updating user!');
            }
        });
        });
//<------------Update User End------------->

          function formatDate(dateString) {
            var date = new Date(dateString);
            return date.toLocaleString();
          }

//<------------Search User start------------->
           // Function to perform the search and update the table
            function searchUsers(searchTerm) {
                // Only trigger search if the searchTerm is not empty
                if (searchTerm.trim() === "") {
                    $('#user-table tbody').html(''); // Clear table if search term is empty
                    return;
                }

                $.ajax({
                    url: `http://localhost:8090/api/v1/users/search?searchTerm=${searchTerm}`,
                    type: 'GET',
                    success: function(response) {
                        if (response.status) {
                            const users = response.data;
                            let rows = '';
                            let rowCount = users.length;

                            // Build the table rows
                            if (rowCount > 0) {
                                rows = loadTableData(users);
                                $('#user-table tbody').html(rows);
                            } else {
                                $('#user-table tbody').html('<tr><td colspan="7">No results found.</td></tr>');
                            }
                        }
                    },
                    error: function() {
                        alert('Error fetching search results.');
                    }
                });
            }

            // Trigger search on keypress in the search input field
            $('#searchInput').on('keyup', function() {
                const searchTerm = $(this).val();
                searchUsers(searchTerm);
            });
//<------------Search User End------------->

//<------------Delete User Start------------->
                // Listen for the delete icon click event
                $(document).on('click', '.deleteUser', function() {
                    var userId = $(this).data('id'); // Get the user ID from the data-id attribute
                    let row = $(this).closest('tr');

                    // Confirm deletion with the user
                    var confirmDelete = confirm("Are you sure you want to delete this user?");
                    if (confirmDelete) {
                        // Send DELETE request to the API
                        $.ajax({
                            url: `http://localhost:8090/api/v1/users/${userId}`, // Endpoint with the user ID
                            type: 'DELETE',
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("X-CSRF-TOKEN", $("#_csrf").val());
                },
                            success: function(response) {
                                if (response.status) {
                                    // If successful, remove the row from the table
                                    alert(response.message); // Show success message
                                    $(row).remove(); // Remove the user row from the table
                                } else {
                                    alert("Failed to delete the user.");
                                }
                            },
                            error: function() {
                                alert('Error deleting the user.');
                            }
                        });
                    }
                });
//<------------Delete User End------------->
      });