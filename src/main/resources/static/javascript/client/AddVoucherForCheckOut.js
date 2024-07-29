const checkAddVoucherPage = document.querySelector('.checkAddVoucher');
if (checkAddVoucherPage) {
    function addVoucherForInvoice() {
        const codeVoucher = document.getElementById('codeVoucherInput').value;
        const totalPayment = parseFloat(document.getElementById('totalPayment').value.replace(/,/g, ''));
        if (codeVoucher === '') {
            const checkInputVoucherList = document.querySelector('input[name="idVoucher"]:checked');
            if (checkInputVoucherList) {
                const id = checkInputVoucherList.value;
                updateMinimumPrice(id);
            } else {
                dangerAlert('Vui lòng chọn Voucher');
            }
        } else {
            const voucherPattern = /^[A-Z0-9]{10}$/;
            if (!voucherPattern.test(codeVoucher)) {
                dangerAlert('Mã Voucher không hợp lệ');
            } else {
                const data = {
                    codeVoucher: codeVoucher,
                    totalPayment: totalPayment,
                }
                $.ajax({
                    type: 'POST',
                    url: 'http://localhost:8080' + '/api/mangostore/voucher-invoice/add',
                    data: JSON.stringify(data),
                    dataType: 'json',
                    contentType: 'application/json',
                    success: function (response) {
                        window.location.href = 'http://localhost:8080/mangostore/cart/checkout';
                    },
                    error: function (e) {
                        if (e.responseText === "1") {
                            dangerAlert('Voucher không tồn tại');
                        } else if (e.responseText === "2") {
                            dangerAlert('Voucher này không thể được sử dụng với hóa đơn này');
                        } else if (e.responseText === '3') {
                            dangerAlert('Voucher đã được sử dụng rồi');
                        } else {
                            errorAlert('Lỗi.');
                        }
                        console.clear();
                    }
                });
            }
        }

        function updateMinimumPrice(id) {
            const minimumPrice = parseFloat(document.getElementById('minimumPrice' + id).value.replace(/,/g, ''));
            if (totalPayment < minimumPrice) {
                dangerAlert('Voucher này không thể được sử dụng với hóa đơn này');
            } else {
                const addVoucherClient = document.getElementById('addVoucherClient');
                addVoucherClient.submit();
            }
        }
    }
}