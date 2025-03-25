var pageName = "refer-friend";
updatePagination(currentPage);

function viewClientData(selected,updatedBy) {
    $('#viewPatientData').find('#rowId').val(selected.id);
    $('#viewPatientData').find('.first-name').text(selected.firstName);
    $('#viewPatientData').find('.last-name').text(selected.lastName);
    $('#viewPatientData').find('.phone-number').text(selected.phoneNumber);
    $('#viewPatientData').find('.email').text(selected.email);
    $('#viewPatientData').find('.dob').text(selected.dateOfBirth);
    $('#viewPatientData').find('.is-employee').text(selected.isEmployee ? 'Yes' : 'No');
    $('#viewPatientData').find('.is-study-participant').text(selected.isStudyParticipant ? 'Yes' : 'No');
    $('#viewPatientData').find('.created-at').text(formatDate(selected.createdAt));
    $('#viewPatientData').find('.updated-at').text(formatDate(selected.updatedAt));
    $('#viewPatientData').find('.updated-by').text(updatedBy);

    $('#viewPatientData').find('.refName').text(selected.refName);
    $('#viewPatientData').find('.refNumber').text(selected.refContactNumber);

    $('#followBack').val(selected.followBack);
    $('#comment').val(selected.comment);
}

$(document).ready(function() {
    updatePagination(currentPage);
});