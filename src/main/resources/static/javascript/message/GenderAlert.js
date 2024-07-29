function dangerAlert(message) {
    return Swal.fire({
        title: "Cảnh báo",
        text: message,
        icon: "info"
    });
}

function successAlert(message) {
    return Swal.fire({
        title: "Thành công",
        text: message,
        icon: "success"
    });
}

function errorAlert(message) {
    return Swal.fire({
        title: "Lỗi !",
        text: message,
        icon: "error"
    });
}

function confirmAlertForm(message1, message2, idForm) {
    Swal.fire({
        title: "Bạn có chắc không ?",
        text: message1,
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Có"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: "Thành công!",
                text: message2,
                icon: "success"
            }).then(() => {
                idForm.submit();
            });
        }
    });
}

function confirmAlertLink(event, message1, message2, url) {
    event.preventDefault();
    Swal.fire({
        title: "Bạn có chắc không?",
        text: message1,
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Có"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: "Thành công!",
                text: message2,
                icon: "success"
            }).then(() => {
                window.location.href = url;
            });
        }
    });
}