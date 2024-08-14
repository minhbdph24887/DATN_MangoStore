const checkVoucherShop = document.querySelector('.voucherShopPage');
if (checkVoucherShop) {
    document.addEventListener('DOMContentLoaded', (event) => {
        const addVoucherButtons = document.querySelectorAll('.addVoucherButton');
        addVoucherButtons.forEach(button => {
            button.addEventListener('click', function () {
                const checkLogin = document.getElementById('checkLogin').value;
                const voucherId = this.getAttribute('data-voucher-id');
                if (checkLogin === 'null') {
                    dangerAlert('Vui lòng đăng nhập vào tài khoản của bạn để lưu phiếu giảm giá');
                } else {
                    const data = {
                        idVoucher: voucherId,
                    }
                    $.ajax({
                        type: 'POST',
                        contentType: 'application/json',
                        url: 'http://localhost:8080' + '/api/mangostore/voucher/add-client',
                        data: JSON.stringify(data),
                        dataType: 'json',
                        success: function (responseData) {
                            if (responseData) {
                                successAlert('Lưu phiếu giảm giá thành công').then((result) => {
                                    if (result.value) {
                                        window.location.href = 'http://localhost:8080/mangostore/voucher/shop';
                                    }
                                });
                            } else {
                                errorAlert('Phiếu giảm giá đã được thêm vào trước đó rồi');
                            }
                        },
                        error: function (e) {
                            errorAlert('Lỗi.');
                            console.clear();
                        }
                    });
                }
            });
        });
    });
}