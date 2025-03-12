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
      <div class="graphs">

        <div class="xs">
          <h3>Update Password</h3>
        </div>

        <div class="clearfix"> </div>

        <div class="col-md-12 pd-0">
          <div class="Compose-Message">
            <div class="panel panel-default">
              <div class="panel-heading">
                Update Password
              </div>
              <form>
                <div class="panel-body"> 
                  <div class="col-md-4">
                    <label> * Enter your new password: </label>
                    <input type="password" class="form-control1" id="password" minlength="6" maxlength="12" placeholder="New Password" required>
                  </div>  

                  <div class="form-group col-md-12">
                    <br>
                    <button type="submit" class="btn btn-primary">Update</button>
                    <button type="reset" class="btn btn-default">Reset</button>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>

        <div class="clearfix"> </div>
        <div class="padding-t-15">
          <?php include 'footer.html'; ?>
        </div>
      </div>
    </div>
  </div>
</body>

</html>