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
                dangerAlert('Please Choose The Voucher');
            }
        } else {
            const voucherPattern = /^[A-Z0-9]{10}$/;
            if (!voucherPattern.test(codeVoucher)) {
                dangerAlert('Voucher Code Is Not Valid');
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
                            dangerAlert('Voucher does not exist');
                        } else if (e.responseText === "2") {
                            dangerAlert('This voucher cannot be used with this receipt');
                        } else if (e.responseText === '3') {
                            dangerAlert('This voucher has been used');
                        } else {
                            errorAlert('An error occurred.');
                        }
                        console.clear();
                    }
                });
            }
        }

        function updateMinimumPrice(id) {
            const minimumPrice = parseFloat(document.getElementById('minimumPrice' + id).value.replace(/,/g, ''));
            console.log('total:', totalPayment);
            console.log('min:', minimumPrice);
            if (totalPayment < minimumPrice) {
                dangerAlert('This voucher Cannot Be Used With This Receipt');
            } else {
                const addVoucherClient = document.getElementById('addVoucherClient');
                addVoucherClient.submit();
            }
        }
    }
}