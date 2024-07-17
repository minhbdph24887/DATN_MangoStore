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
            dangerAlert('You can only purchase a maximum of 50 products/items');
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
            dangerAlert('Please Login to your account to Add To Cart');
        } else {
            const idProductDetail = document.getElementById('idProductDetail').value;
            const idProduct = document.getElementById('idProduct').value;
            const quantityNew = document.querySelector('.quantity-selector input[type= "number"]').value;

            const colorElements = document.querySelectorAll('#color-options .option.active');
            const sizeElements = document.querySelectorAll('#size-options .option.active');
            let idColor = colorElements.length > 0 ? colorElements[0].dataset.value : 0;
            let idSize = sizeElements.length > 0 ? sizeElements[0].dataset.value : 0;
            if (quantityNew > availableQuantity) {
                errorAlert('The added quantity must not exceed the actual quantity of the product');
            } else {
                AddToCartAPI(idProductDetail, idProduct, idSize, idColor, quantityNew);
            }
        }
    }

    function AddToFavourite() {
        const checkLogin = document.getElementById('checkLogin').value;
        if (checkLogin === 'null') {
            dangerAlert('Please Login to your account to Add To Favourite');
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
        }
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080' + '/api/mangostore/add-to-cart/client',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json',
            success: function (response) {
                if (response) {
                    successAlert('Add To Cart Successfully').then((result) => {
                        if (result.value) {
                            window.location.href = 'http://localhost:8080/mangostore/product/detail/' + idProductDetail;
                        }
                    });
                } else {
                    errorAlert("Login has expired, please log in again").then((result) => {
                        if (result.value) {
                            window.location.href = "http://localhost:8080/mangostore/home";
                        }
                    });
                }
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
                    successAlert('Add To Favourite Successfully').then((result) => {
                        if (result.value) {
                            window.location.href = 'http://localhost:8080/mangostore/product/detail/' + idProductDetail;
                        }
                    });
                } else {
                    errorAlert("you already like that product").then((result) => {
                        if (result.value) {
                            window.location.href = "http://localhost:8080/mangostore/product/detail/" + idProductDetail;
                        }
                    });
                }
            },
            error: function (error) {
                errorAlert('An error occurred');
                console.log(error);
            }
        });
    }
}