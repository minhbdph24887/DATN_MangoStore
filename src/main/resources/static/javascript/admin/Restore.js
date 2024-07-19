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
            dangerAlert("The new quantity is incorrect, please re-enter.");
            return;
        }
    }

    const data = {
        idProductDetail: idProductDetail,
        quantity: quantity,
    };


        $.ajax({
            type: "POST",
            url: "http://localhost:8080" + "/api/mangostore/admin/product-detail/restore",
            data: JSON.stringify(data),
            contentType: 'application/json',
            dataType: 'json',
            success: function (response) {
                successAlert('Khôi Phục Thành Công').then(() => {
                    window.open("http://localhost:8080/mangostore/admin/product-detail", "_self")
                });
            },
            error: function (error) {
                dangerAlert('Lỗi')
                console.clear();
            }
        });

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

const restoreCategoryPage = document.querySelector('.restoreCategoryPage');
if (restoreCategoryPage) {
    function restoreCategory(button){
        const idCategory = button.getAttribute("data-id");
        const url = "/mangostore/admin/category/restore/" + idCategory;
        confirmAlertLink(event,"Do you want to restore?","Successful recovery", url)
    }
}

const restoreColorPage = document.querySelector('.restoreColorPage');
if (restoreColorPage) {
    function restoreColor(button){
        const idColor = button.getAttribute("data-id");
        const url = "/mangostore/admin/color/restore/" + idColor;
        confirmAlertLink(event, "Do you want to restore?", "Successful recovery", url)
    }
}

const restoreMaterialPage = document.querySelector('.restoreMaterialPage');
if (restoreMaterialPage) {
    function restoreMaterial(button){
        const idMaterial = button.getAttribute("data-id");
        const url = "/mangostore/admin/material/restore/" + idMaterial;
        confirmAlertLink(event, "Do you want to restore?", "Successful recovery", url);
    }
}

const restoreSizePage = document.querySelector('.restoreSizePage');
if (restoreSizePage) {
    function restoreSize(button){
        const idSize = button.getAttribute("data-id");
        const url = "/mangostore/admin/size/restore/" + idSize;
        confirmAlertLink(event, "Do you want to restore?", "Successful recovery", url);
    }
}

const restoreOriginPage = document.querySelector('.restoreOriginPage');
if (restoreOriginPage) {
    function restoreOrigin(button){
        const idOrigin = button.getAttribute("data-id");
        const url = "/mangostore/admin/origin/restore/" + idOrigin;
        confirmAlertLink(event, "Do you want to restore?", "Successful recovery", url);
    }
}

const restoreProductPage = document.querySelector('.restoreProductPage');
if (restoreProductPage) {
    function restoreProduct(button){
        const idProduct = button.getAttribute("data-id");
        const url = "/mangostore/admin/product/restore/" + idProduct;
        confirmAlertLink(event, "Do you want to restore?", "Successful recovery", url);
    }
}





