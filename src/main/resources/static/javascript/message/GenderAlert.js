function dangerAlert(message) {
    return Swal.fire({
        title: "Warning",
        text: message,
        icon: "info"
    });
}

function successAlert(message) {
    return Swal.fire({
        title: "Successfully",
        text: message,
        icon: "success"
    });
}

function errorAlert(message) {
    return Swal.fire({
        title: "Error!",
        text: message,
        icon: "error"
    });
}