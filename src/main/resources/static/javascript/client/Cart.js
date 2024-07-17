const checkCartPage = document.querySelector(".checkCartPage");
if (checkCartPage) {
    function checkCartIndex() {
        const checkLogin = document.getElementById('checkLogin').value;
        if (checkLogin === 'null') {
            dangerAlert('Please log in to go to the shopping cart page');
        } else {
            window.location.href = "http://localhost:8080/mangostore/cart";
        }
    }
}

const checkQuantityCartPage = document.querySelector('.checkQuantityProductDetail');
if (checkQuantityCartPage) {
    function increaseQuantityForCart(idProductDetail) {
        const quantityNew = document.getElementById('quantityInput').value;
        const data = {
            idProductDetail: idProductDetail,
            quantityNew: quantityNew,
        }
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080' + '/api/mangostore/cart/increase',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json',
            success: function (response) {
                const increaseFrom = document.getElementById('increaseFrom');
                increaseFrom.submit();
            },
            error: function (e) {
                if (e.responseText === '1') {
                    dangerAlert('The quantity added must not exceed the quantity in stock');
                } else if (e.responseText === '2') {
                    dangerAlert('You can only purchase a maximum of 50 products/items');
                }
                console.clear();
            }
        });
    }
}