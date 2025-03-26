var pageName = "become-principal-investigator";
updatePagination(currentPage);

function viewClientData(selected,updatedBy) {
    $('#viewPatientData').find('#rowId').val(selected.id);
    $('#viewPatientData').find('.first-name').text(selected.firstName);
    $('#viewPatientData').find('.last-name').text(selected.lastName);
    $('#viewPatientData').find('.phone-number').text(selected.phoneNumber);
    $('#viewPatientData').find('.email').text(selected.email);
    $('#viewPatientData').find('.speciality').text(selected.speciality);
    $('#viewPatientData').find('.created-at').text(formatDate(selected.createdAt));
    $('#viewPatientData').find('.updated-at').text(formatDate(selected.updatedAt));
    $('#viewPatientData').find('.updated-by').text(updatedBy);
    $('#followBack').val(selected.followBack);
    $('#comment').val(selected.comment);
}

$(document).ready(function() {
    updatePagination(currentPage);
});