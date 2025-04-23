// Initialize the Quill editor
// var quill = new Quill('#editor', {
//     theme: 'snow'
//   });

//  const toolbarOptions = [
//    ['bold', 'italic', 'underline'],
//    ['link'],
//
//    [{ 'header': 1 }, { 'header': 2 }],
//    [{ 'script': 'sub'}, { 'script': 'super' }],
//
//    [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
//
//
//    ['clean']
//  ];
//    $("#updateContentSG").val(quill.root.innerHTML.replaceAll("<p>","").replaceAll("</p>","").replaceAll("&nbsp;",""));

  const toolbarOptions = [
    ['bold', 'italic', 'underline'],
    ['link'],
    [{ 'script': 'sub'}, { 'script': 'super' }],

    ['clean']
  ];


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
//      var content = quill.root.innerHTML;
//      $('#output').val(content);
    $("#updateContentSG").val(quill.root.innerHTML.replaceAll("&nbsp;","").replaceAll("<p><br></p>",""));

  }

$('.contentTypeSG').on('change', function () {
    activateEditor(this);

    checkImageField($(this).val(), $(this).parent().parent());
});

var quill;
function activateEditor(thisVar) {
  const selectedValue = $(thisVar).val();
  const $form = $(thisVar).closest('form');

  const $quillWrapper = $form.find('.quillEditor'); // The div for Quill
  const $textAreaWrapper = $form.find('.contentSG'); // The original textarea wrapper
  const $hiddenTextarea = $textAreaWrapper.find('textarea'); // The textarea inside .contentSG

  if (selectedValue === "BLOG") {
    console.log("BLOG selected!");

    const quillContainer = $quillWrapper[0];

    // Prevent reinitialization
    if (!quillContainer.classList.contains('ql-container')) {
        quill = new Quill(quillContainer, {
        modules: {
          toolbar: toolbarOptions
        },
        theme: 'snow'
      });



      // Optional: Sync on form submit
      $form.on('submit', function () {
        $textAreaWrapper.val(quill.root.innerHTML);
      });
    } else {
        $form.find('.ql-toolbar').show();
    }
          // Optional: Load value from textarea if needed
          const initialVal = $textAreaWrapper.val();
          if (initialVal) {
            quill.root.innerHTML = initialVal;
          }

              $quillWrapper.show();
              $textAreaWrapper.hide();

  } else {
    // Clear quill content if needed
    if ($quillWrapper.find('.ql-editor').length) {
      $quillWrapper.find('.ql-editor').html('');
      $form.find('.ql-toolbar').hide();
    }

    $quillWrapper.hide();
  }
}

