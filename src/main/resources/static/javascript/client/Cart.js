const checkCartPage = document.querySelector(".checkCartPage");
if (checkCartPage) {
    function checkCartIndex() {
        const checkLogin = document.getElementById('checkLogin').value;
        if (checkLogin === 'null') {
            dangerAlert('Vui lòng đăng nhập để đến trang giỏ hàng');
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
                    dangerAlert('Số lượng thêm vào không được vượt quá số lượng tồn');
                } else if (e.responseText === '2') {
                    dangerAlert('Bạn chỉ có thể mua tối đa 50 sản phẩm/món hàng');
                }
                console.clear();
            }
        });
    }
}