const checkVoucherShop = document.querySelector('.voucherShopPage');
if (checkVoucherShop) {
    document.addEventListener('DOMContentLoaded', (event) => {
        const addVoucherButtons = document.querySelectorAll('.addVoucherButton');
        addVoucherButtons.forEach(button => {
            button.addEventListener('click', function () {
                const checkLogin = document.getElementById('checkLogin').value;
                const voucherId = this.getAttribute('data-voucher-id');
                if (checkLogin === 'null') {
                    dangerAlert('Please log in to your account to save your voucher');
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
                                successAlert('Save Voucher Successfully').then((result) => {
                                    if (result.value) {
                                        window.location.href = 'http://localhost:8080/mangostore/voucher/shop';
                                    }
                                });
                            } else {
                                errorAlert('The voucher has already been added');
                            }
                        },
                        error: function (e) {
                            errorAlert('An error occurred.');
                            console.log(e);
                        }
                    });
                }
            });
        });
    });
}