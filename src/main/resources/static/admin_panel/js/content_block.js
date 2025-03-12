$('.openContentBlock').click(function(){
        $("#content_blocks-tab").click();
}); 

$('#addContentBlockFunction').click(function () {
    $('#addContentBlock').modal('show');
});

$('.updateContentBlock').click(function () {
    $('#updateContentBlock').modal('show');
});


$('.deleteContentBlock').click(function () {
    if (confirm("Are you sure to delete this page!")) {


    }
});