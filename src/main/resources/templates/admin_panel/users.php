<!DOCTYPE HTML>
<html>

<head>
  <?php include 'header.html'; ?>
</head>

<body>
  <div id="wrapper">
    <!-- Start Navigation -->
    <?php include 'navigation.html'; ?>
    <!-- End Navigation -->

    <div id="page-wrapper">
      <div class=" graphs">

        <div class="xs">
          <h3>Users</h3>
        </div>

        <div class="clearfix"> </div>

        <div class="grid_3 grid_5">
          <div class="but_list mg-t-0">
            <div class="bs-example bs-example-tabs" role="tabpanel" data-example-id="togglable-tabs">

              <div id="myTabContent" class="tab-content mg-t-0">

                <form action="#" method="GET">
                  <div class="input-group">
                    <input type="text" name="search" class="form-control1 input-search" placeholder="Search...">
                    <span class="input-group-btn">
                      <button class="btn btn-success" type="button"><i class="fa fa-search"></i></button>
                    </span>
                  </div> 
                </form>

                <div role="tabpanel" class="tab-pane fade active in" id="pages" aria-labelledby="pages-tab">
                  <div class=" pd-0">
                    <div class="mailbox-content pd-0">
                      <div class="mail-toolbar clearfix pd-t-0">
                        <div class="float-left pd-t-20">
                          <div class="btn btn_1 btn-default mrg5R" data-toggle="modal" data-target="#addUser">
                            <i class="fa fa-plus"> </i>
                          </div>

                          <div class="btn btn_1 btn-default mrg5R">
                            <i class="fa fa-refresh"> </i>
                          </div>

                          <div class="clearfix"> </div>
                        </div>
                        <div class="float-right pd-t-20">

                          <span class="text-muted m-r-sm">Showing 10 of 25 </span>
                          <div class="btn-group">
                            <a class="btn btn-default"><i class="fa fa-angle-left"></i></a>
                            <a class="btn btn-default"><i class="fa fa-angle-right"></i></a>
                          </div>
                        </div>

                      </div>
                      <table class="table">
                        <thead>
                          <tr>
                            <!-- <th>#</th> -->
                            <th>Id</th>
                            <th>Username</th>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Email</th>
                            <th>Created</th>
                            <th>Updated</th>
                            <th> </th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr>
                            <td>
                              1
                            </td>
                            <td class="hidden-xs">
                              <a href="#" onclick="return false;" class="openContentBlock">
                                AJakati
                              </a>
                            </td>
                            <td>
                              Abdulrazzak
                            </td>
                            <td>
                              Jakati
                            </td>
                            <td>
                              Abdulrazzak.Jakati@gmail.com
                            </td>
                            <td>
                              02/March/2025
                            </td>
                            <td>
                              06/March/2025
                            </td>
                            <td>
                              <i class="fa fa-edit text-success text-active updateUser"></i> <br>
                              <i class="fa fa-times text-danger text deletePage"></i>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>

                <div class="clearfix"> </div>
              </div>
            </div>
          </div>
        </div>

        <div class="clearfix"> </div>
        <div class="padding-t-15">
          <?php include 'footer.html'; ?>
        </div>
      </div>

    </div>

    <script src="js/users.js"></script>

    <!-------------------- Slide-Content Code Block Start -------------------->
    <!-- Add Slide Content PopUp Start -->
    <div class="modal fade" id="addUser" tabindex="-1" role="dialog" aria-labelledby="addUserLabel" aria-hidden="true"
      style="display: none;">
      <div class="modal-dialog">
        <div class="modal-content">
          <form>
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
              <h2 class="modal-title text-center">Add User</h2>
            </div>
            <div class="modal-body">
              <div class="panel-body">
                <label>Select Role: </label>
                <select name="selector1" id="selector1" class="form-control1 control3" required>
                  <option value="">Select Role</option>
                  <option>SLide Block 1</option>
                  <option>SLide Block 2</option>
                </select>

                <label>Enter Username: </label>
                <input type="text" class="form-control1 control3" required>

                <label>Enter Password: </label>
                <input type="password" minlength="6" maxlength="12" class="form-control1 control3" required>

                <label>Enter First Name</label>
                <input type="text" class="form-control1 control3" required>

                <label>Enter Last Name</label>
                <input type="text" class="form-control1 control3" required>

                <label>Enter Email</label>
                <input type="email" class="form-control1 control3" required>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
              <button type="submit" class="btn btn-primary">Save</button>
              <button type="reset" class="btn btn-primary">Reset</button>
            </div>
          </form>
        </div>
      </div>
    </div>
    <!-- Add Slide Content PopUp End -->

    <!-- Update Slide Content PopUp Start -->
    <div class="modal fade" id="updateUser" tabindex="-1" role="dialog" aria-labelledby="updateSlideContentLabel"
      aria-hidden="true" style="display: none;">
      <div class="modal-dialog">
        <div class="modal-content">
          <form>
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
              <h2 class="modal-title text-center">Update User</h2>
            </div>
            <div class="modal-body">
              <div class="panel-body">
                <label>Select Role: </label>
                <select name="selector1" id="selector1" class="form-control1 control3" required>
                  <option value="">Select Role</option>
                  <option>SLide Block 1</option>
                  <option>SLide Block 2</option>
                </select>

                <label>Enter Username: </label>
                <input type="text" class="form-control1 control3" required>

                <label>Enter Password: </label>
                <input type="password" minlength="6" maxlength="12" class="form-control1 control3" required>

                <label>Enter First Name</label>
                <input type="text" class="form-control1 control3" required>

                <label>Enter Last Name</label>
                <input type="text" class="form-control1 control3" required>

                <label>Enter Email</label>
                <input type="email" class="form-control1 control3" required>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
              <button type="submit" class="btn btn-primary">Save</button>
            </div>
          </form>
        </div>
      </div>
    </div>
    <!-- Add Slide PopUp End -->
    <!-------------------- Slide Code Block End -------------------->

  </div>

</body>

</html>