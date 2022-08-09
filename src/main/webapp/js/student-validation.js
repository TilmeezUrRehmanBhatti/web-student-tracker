function validateForm() {

    // error field
    var theErrorField = [];

    // student form
    var studentForm = document.forms["studentForm"];

    // Check first name
    var firstName = studentForm["firstName"].value.trim();;
    if (firstName == "") {
        theErrorField.push("First name");
    }

    // check last name
    var lastName = studentForm["lastName"].value.trim();;
    if (lastName == "") {
        theErrorField.push("Last name");
    }

    // check email
    var email = studentForm["email"].value.trim();;
    if (email == "") {
        theErrorField.push("Email");
    }
    if (theErrorField.length > 0) {
        alert("Form validation failed, Please add data for the following field: " + theErrorField);
        return false;
    }
}