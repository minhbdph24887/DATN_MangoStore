function restoreProductDetail(button) {
    const tdElement = button.parentElement;
    const idProductDetail = tdElement.getElementsByClassName("idProductDetail")[0].value;
    const oldQuantityElement = tdElement.getElementsByClassName("quantityOld")[0];
    const newQuantityElement = tdElement.getElementsByClassName("quantityNew")[0];
    let quantity;

    if (oldQuantityElement && window.getComputedStyle(oldQuantityElement).display !== "none") {
        quantity = oldQuantityElement.value;
    } else if (newQuantityElement && window.getComputedStyle(newQuantityElement).display !== "none") {
        quantity = newQuantityElement.value;
        if (quantity <= 0) {
            alert("The new quantity is incorrect, please re-enter.");
            return;
        }
    }

    const data = {
        idProductDetail: idProductDetail,
        quantity: quantity,
    };

    if (restore()) {
        alert("Restore ProductDetail Fall.");
    } else {
        $.ajax({
            type: "POST",
            url: "http://localhost:8080" + "/api/mangostore/admin/product-detail/restore",
            data: JSON.stringify(data),
            contentType: 'application/json',
            dataType: 'json',
            success: function (response) {
                window.open("http://localhost:8080/mangostore/admin/product-detail", "_self")
            },
            error: function (error) {
                console.log(error);
            }
        });
    }
}

function restoreVoucher(button) {
    const tdElement = button.parentElement;
    const idVoucher = tdElement.getElementsByClassName("idVoucher")[0].value;
    const oldQuantityElement = tdElement.getElementsByClassName("quantityOld")[0];
    const newQuantityElement = tdElement.getElementsByClassName("quantityNew")[0];
    let quantity;

    if (oldQuantityElement && window.getComputedStyle(oldQuantityElement).display !== "none") {
        quantity = oldQuantityElement.value;
    } else if (newQuantityElement && window.getComputedStyle(newQuantityElement).display !== "none") {
        quantity = newQuantityElement.value;
        if (quantity <= 0) {
            alert("The new quantity is incorrect, please re-enter.");
            return;
        }
    }

    const data = {
        idVoucher: idVoucher,
        quantity: quantity,
    };

    if (restore()) {
        alert("Restore Voucher Fall.");
    } else {
        $.ajax({
            type: "POST",
            url: "http://localhost:8080" + "/api/mangostore/admin/voucher/restore",
            data: JSON.stringify(data),
            contentType: 'application/json',
            dataType: 'json',
            success: function (response) {
                window.open("http://localhost:8080/mangostore/admin/voucher", "_self")
            },
            error: function (error) {
                console.log(error);
            }
        });
    }
}
