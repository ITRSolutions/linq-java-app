$(document).ready(function () {
  $("#submit").on("click", function (e) {
    let isValid = true;
    $(".error-msg").text(""); // clear old errors

    // Validate name
    const name = $("#name").val().trim();
    if (name.length < 4 || name.length > 50) {
      $("#name").next(".error-msg").text("Name must be between 4 and 50 characters.");
      isValid = false;
    }

    // Validate email
    const email = $("#email").val().trim();
    const emailPattern = /^[^@\s]+@[^@\s]+\.[^@\s]+$/;
    if (!emailPattern.test(email)) {
      $("#email").next(".error-msg").text("Please enter a valid email address.");
      isValid = false;
    }

    // Validate phone
    const phone = $("#phone").val().trim();
    if (phone === "") {
      $("#phone").next(".error-msg").text("Phone is required.");
      isValid = false;
    }

    // Validate resume file
    const resume = $("input[name='resume']")[0].files[0];
    if (!resume || resume.type !== "application/pdf" || resume.size > 1024 * 1024) {
      $("input[name='resume']").siblings(".error-msg").text("Upload a PDF under 1MB.");
      isValid = false;
    }

    // Validate cover letter file
    const coverLetter = $("input[name='coverLetter']")[0].files[0];
    if (!coverLetter || coverLetter.type !== "application/pdf" || coverLetter.size > 1024 * 1024) {
      $("input[name='coverLetter']").siblings(".error-msg").text("Upload a PDF under 1MB.");
      isValid = false;
    }

    // Validate radio groups
if (!$("input[name='eligibility']:checked").val()) {
  $("input[name='eligibility']").closest(".form-group").find(".error-msg").text("Please select an option.");
  isValid = false;
}

if (!$("input[name='visa']:checked").val()) {
  $("input[name='visa']").closest(".form-group").find(".error-msg").text("Please select an option.");
  isValid = false;
}

    // Optional: Validate compensation
    const compensation = $("#compensation").val().trim();
    if (compensation.length < 5 || compensation.length > 100) {
      $("#compensation").next(".error-msg").text("Must be between 5 and 100 characters.");
      isValid = false;
    }

if (!$("input[name='reside']:checked").val()) {
  $("input[name='reside']").closest(".form-group").find(".error-msg").text("Please select an option.");
  isValid = false;
} console.log("isValid: "+isValid);
    if (!isValid) {
    e.preventDefault(); // prevent form submission
    $(".error-msg").show();
    }
  });
});

  function validateFile(file) {
    return file.type === "application/pdf" && file.size <= 1 * 1024 * 1024;
  }

  function handleFile($container, file) {
    const $error = $container.find('.error-msg');
    const $fileName = $container.find('.file-name');

    if (!validateFile(file)) {
      $error.show();
      $fileName.text('');
      $container.find('.upload-input').val('');
    } else {
      $error.hide();

      $fileName.text(`Selected file: ${file.name}`);
    }
  }

  $('.file-upload').each(function () {
    const $box = $(this);
    const $input = $box.find('.upload-input');

    // Open file picker when clicking the custom button
    $box.find('.upload-btn').on('click', function () {
      $input.trigger('click');
    });

    $input.on('change', function () {
      if (this.files.length > 0) {
        handleFile($box, this.files[0]);
      }
    });

    $box.on('dragover', function (e) {
      e.preventDefault();
      e.stopPropagation();
      $box.addClass('dragover');
    });

    $box.on('dragleave', function () {
      $box.removeClass('dragover');
    });

    $box.on('drop', function (e) {
      e.preventDefault();
      $box.removeClass('dragover');
      const droppedFile = e.originalEvent.dataTransfer.files[0];
      if (droppedFile) {
        $input[0].files = e.originalEvent.dataTransfer.files;
        handleFile($box, droppedFile);
      }
    });
  });