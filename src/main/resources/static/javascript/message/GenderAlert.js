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

function confirmAlertForm(message1, message2, idForm) {
    Swal.fire({
        title: "Are you sure?",
        text: message1,
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: "Successfully!",
                text: message2,
                icon: "success"
            }).then(() => {
                idForm.submit();
            });
        }
    });
}

function confirmAlertLink(message1, message2, url) {
    Swal.fire({
        title: "Are you sure?",
        text: message1,
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: "Successfully!",
                text: message2,
                icon: "success"
            }).then(() => {
                window.location.href = url;
            });
        }
    });
}