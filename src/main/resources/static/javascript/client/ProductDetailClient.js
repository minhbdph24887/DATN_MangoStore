const checkPageProductDetailClient = document.querySelector(".showImagePage");
if (checkPageProductDetailClient) {
    let availableQuantity = 0;
    const checkButtonAddToCart = document.getElementById("checkButtonAddToCart");
    const checkButtonFavourite = document.getElementById("checkButtonFavourite");
    checkButtonAddToCart.disabled = true;
    checkButtonFavourite.disabled = true;

    $(document).ready(function () {
        $('.nav-link').click(function () {
            $('.nav-link').removeClass('active');
            $('.tab-pane').removeClass('active');
            $(this).addClass('active');
            $($(this).data('bs-target')).addClass('active');
        });
    });

    $(document).ready(function () {
        $(".option-group .option").click(function () {
            $(this).siblings().removeClass("active");
            $(this).addClass("active");
        });
    });

    document.getElementById('button-minus').addEventListener('click', function () {
        const input = document.querySelector('.quantity-selector input[type="number"]');
        const currentValue = parseInt(input.value, 10);
        if (currentValue > 1) {
            input.value = currentValue - 1;
        }
    });

    document.getElementById('button-plus').addEventListener('click', function () {
        const input = document.querySelector('.quantity-selector input[type="number"]');
        const currentValue = parseInt(input.value, 10);
        if (currentValue >= 50) {
            dangerAlert('Bạn chỉ có thể mua tối đa 50 sản phẩm/món hàng');
        } else {
            input.value = currentValue + 1;
        }
    });

    document.addEventListener('DOMContentLoaded', (event) => {
        const colorElements = document.querySelectorAll('#color-options .option');
        const sizeElements = document.querySelectorAll('#size-options .option');
        const idProduct = document.getElementById('idProduct').value;
        let color = 0;
        let size = 0;

        colorElements.forEach((element) => {
            element.addEventListener('click', (event) => {
                color = event.target.dataset.value;
                findProductByVariant(idProduct, color, size);
            });
        });

        sizeElements.forEach((element) => {
            element.addEventListener('click', (event) => {
                size = event.target.dataset.value;
                findProductByVariant(idProduct, color, size);
            });
        });
    });

    function findProductByVariant(idProduct, color, size) {
        const data = {
            idProduct: idProduct,
            idSize: size,
            idColor: color,
        };

        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/api/mangostore/find-product-by-variant',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json',
            success: function (response) {
                if (response.price !== undefined) {
                    $('#showPrice').css('color', 'orange');
                    $('#showPrice').text(new Intl.NumberFormat('vi-VN', {
                        style: 'currency',
                        currency: 'VND',
                        currencyDisplay: 'code',
                        minimumFractionDigits: 0
                    }).format(response.price).replace('VND', '').trim() + ' VND');
                }

                if (response.quantity === 0) {
                    $('#showQuantity').text('Sản phẩm đã hết');
                    disableAllButtons(true);
                } else if (response.quantity !== undefined) {
                    $('#showQuantity').text('Còn: ' + response.quantity + ' sản phẩm');
                    availableQuantity = response.quantity;
                    disableAllButtons(false);
                } else if (response.message) {
                    $('#showQuantity').text(response.message);
                    disableAllButtons(true);
                } else {
                    $('#showQuantity').text('Dữ liệu sản phẩm không hợp lệ');
                    disableAllButtons(true);
                }
            },
            error: function (xhr, status, error) {
                if (xhr.status === 404) {
                    $('#showQuantity').text('Sản phẩm không tồn tại');
                    disableAllButtons(true);
                } else {
                    console.error('An error occurred: ' + error);
                }
            }
        });
    }

    function disableAllButtons(boolean) {
        document.getElementById('button-minus').disabled = boolean;
        document.getElementById('button-plus').disabled = boolean;
        checkButtonAddToCart.disabled = boolean;
        checkButtonFavourite.disabled = boolean;
    }


    function AddToCart() {
        const checkLogin = document.getElementById('checkLogin').value;
        if (checkLogin === 'null') {
            dangerAlert('Vui lòng đăng nhập vào tài khoản của bạn để Thêm vào giỏ hàng');
        } else {
            const idProductDetail = document.getElementById('idProductDetail').value;
            const idProduct = document.getElementById('idProduct').value;
            const quantityNew = document.querySelector('.quantity-selector input[type= "number"]').value;

            const colorElements = document.querySelectorAll('#color-options .option.active');
            const sizeElements = document.querySelectorAll('#size-options .option.active');
            let idColor = colorElements.length > 0 ? colorElements[0].dataset.value : 0;
            let idSize = sizeElements.length > 0 ? sizeElements[0].dataset.value : 0;
            if (quantityNew > availableQuantity) {
                errorAlert('Số lượng thêm không được vượt quá số lượng thực tế của sản phẩm');
            } else {
                AddToCartAPI(idProductDetail, idProduct, idSize, idColor, quantityNew);
            }
        }
    }

    function AddToFavourite() {
        const checkLogin = document.getElementById('checkLogin').value;
        if (checkLogin === 'null') {
            dangerAlert('Vui lòng đăng nhập vào tài khoản của bạn để thêm vào yêu thích');
        } else {
            const idProductDetail = document.getElementById('idProductDetail').value;
            const idProduct = document.getElementById('idProduct').value;

            const colorElements = document.querySelectorAll('#color-options .option.active');
            const sizeElements = document.querySelectorAll('#size-options .option.active');
            let idColor = colorElements.length > 0 ? colorElements[0].dataset.value : 0;
            let idSize = sizeElements.length > 0 ? sizeElements[0].dataset.value : 0;
            AddToFavouriteAPI(idProductDetail, idProduct, idColor, idSize);
        }
    }

    function AddToCartAPI(idProductDetail, idProduct, idSize, idColor, quantityNew) {
        const data = {
            idProduct: idProduct,
            idSize: idSize,
            idColor: idColor,
            quantity: quantityNew,
        };
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080' + '/api/mangostore/add-to-cart/client',
            data: JSON.stringify(data),
            contentType: 'application/json',
            success: function (response) {
                successAlert(response).then((result) => {
                    if (result.value) {
                        window.location.href = 'http://localhost:8080/mangostore/product/detail/' + idProductDetail;
                    }
                });
            },
            error: function (e) {
                if (e.status === 400 && e.responseText === "1") {
                    errorAlert("Đăng nhập đã hết hạn, vui lòng đăng nhập lại").then((result) => {
                        if (result.value) {
                            window.location.href = "http://localhost:8080/mangostore/home";
                        }
                    });
                } else {
                    dangerAlert(e.responseText);
                }
                console.clear();
            }
        });
    }

    function AddToFavouriteAPI(idProductDetail, idProduct, idColor, idSize) {
        const data = {
            idProduct: idProduct,
            idSize: idSize,
            idColor: idColor,
        }
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080' + '/api/mangostore/add-to-favourite/client',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json',
            success: function (response) {
                if (response) {
                    successAlert('Thêm vào mục yêu thích thành công').then((result) => {
                        if (result.value) {
                            window.location.href = 'http://localhost:8080/mangostore/product/detail/' + idProductDetail;
                        }
                    });
                } else {
                    errorAlert("Bạn đã thích sản phẩm đó rồi").then((result) => {
                        if (result.value) {
                            window.location.href = "http://localhost:8080/mangostore/product/detail/" + idProductDetail;
                        }
                    });
                }
            },
            error: function (error) {
                errorAlert('Lỗi');
                console.clear();
            }
        });
    }
}