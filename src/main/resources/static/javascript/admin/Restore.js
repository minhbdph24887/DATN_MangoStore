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
            dangerAlert("Số lượng mới sai, vui lòng nhập lại");
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
            successAlert('Khôi phục thành công').then(() => {
                window.open("http://localhost:8080/mangostore/admin/product-detail", "_self")
            });
        },
        error: function (error) {
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
            dangerAlert("Số lượng mới sai, vui lòng nhập lại");
            return;
        }
    }

    const data = {
        idVoucher: idVoucher,
        quantity: quantity,
    };

    $.ajax({
        type: "POST",
        url: "http://localhost:8080" + "/api/mangostore/admin/voucher/restore",
        data: JSON.stringify(data),
        contentType: 'application/json',
        dataType: 'json',
        success: function (response) {
            successAlert('Khôi Phục Voucher Thành Công').then(() => {
                window.open("http://localhost:8080/mangostore/admin/voucher", "_self")
            });
        },
        error: function (error) {
            dangerAlert('Lỗi')
            console.clear();
        }
    });
}

const restoreCategoryPage = document.querySelector('.restoreCategoryPage');
if (restoreCategoryPage) {
    function restoreCategory(button) {
        const idCategory = button.getAttribute("data-id");
        const url = "/mangostore/admin/category/restore/" + idCategory;
        confirmAlertLink(event, "Bạn có muốn khôi phục danh mục này ?", "Khôi phục thành công", url)
    }
}

const restoreColorPage = document.querySelector('.restoreColorPage');
if (restoreColorPage) {
    function restoreColor(button) {
        const idColor = button.getAttribute("data-id");
        const url = "/mangostore/admin/color/restore/" + idColor;
        confirmAlertLink(event, "Bạn có muốn khôi phục màu sắc này ?", "Khôi phục thành công", url)
    }
}

const restoreMaterialPage = document.querySelector('.restoreMaterialPage');
if (restoreMaterialPage) {
    function restoreMaterial(button) {
        const idMaterial = button.getAttribute("data-id");
        const url = "/mangostore/admin/material/restore/" + idMaterial;
        confirmAlertLink(event, "Bạn có muốn khôi phục chất liệu này ?", "Khôi phục thành công", url);
    }
}

const restoreSizePage = document.querySelector('.restoreSizePage');
if (restoreSizePage) {
    function restoreSize(button) {
        const idSize = button.getAttribute("data-id");
        const url = "/mangostore/admin/size/restore/" + idSize;
        confirmAlertLink(event, "Bạn có muốn khôi phục kích cỡ này ?", "Khôi phục thành công", url);
    }
}

const restoreOriginPage = document.querySelector('.restoreOriginPage');
if (restoreOriginPage) {
    function restoreOrigin(button) {
        const idOrigin = button.getAttribute("data-id");
        const url = "/mangostore/admin/origin/restore/" + idOrigin;
        confirmAlertLink(event, "Bạn có muốn khôi phục nhà sản xuất này?", "Khôi phục thành công", url);
    }
}

const restoreProductPage = document.querySelector('.restoreProductPage');
if (restoreProductPage) {
    function restoreProduct(button) {
        const idProduct = button.getAttribute("data-id");
        const url = "/mangostore/admin/product/restore/" + idProduct;
        confirmAlertLink(event, "Bạn có muốn khôi phục sản phẩm này ?", "Khôi phục thành công", url);
    }
}
