// Initialize the Quill editor
// var quill = new Quill('#editor', {
//     theme: 'snow'
//   });

  const toolbarOptions = [
    ['bold', 'italic', 'underline'],
    ['link'],

    [{ 'header': 1 }, { 'header': 2 }],
    [{ 'script': 'sub'}, { 'script': 'super' }],

    [{ 'header': [1, 2, 3, 4, 5, 6, false] }],


    ['clean']
  ];

  const quill = new Quill('#editor', {
    modules: {
      toolbar: toolbarOptions
    },
    theme: 'snow'
  });


  $('#custom-color-button').on('click', function() {
    $('#color-picker').click();
  });

  $('#color-picker').on('input', function() {
    var color = $(this).val();
    var range = quill.getSelection();
    if (range) {
      quill.format('color', color);
    }
    $('#custom-color-button').css('background-color', color);
  });

  // Function to get the content of the editor as HTML and set it to the textarea
  function getEditorContent() {
      var content = quill.root.innerHTML;
      $('#output').val(content);
  }