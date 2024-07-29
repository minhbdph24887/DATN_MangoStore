document.addEventListener('DOMContentLoaded', (event) => {
    const checkOutPage = document.querySelector(".checkOutPage");
    if (checkOutPage) {
        const invoiceCreationDateElement = document.getElementById('checkCreateInvoice');
        const invoiceCreationDate = invoiceCreationDateElement.textContent;
        const invoiceCreationMoment = moment(invoiceCreationDate, 'HH:mm:ss : DD/MM/YYYY');
        let alertShown = false;

        function checkInvoiceExpiry() {
            const now = moment();
            if (now.diff(invoiceCreationMoment, 'minutes') >= 30) {
                if (!alertShown) {
                    alertShown = true;
                    dangerAlert('Hóa đơn đã hết hạn, vui lòng tạo hóa đơn mới').then((result) => {
                        if (result.value) {
                            window.location.href = 'http://localhost:8080/mangostore/home';
                        }
                    });
                }
                clearInterval(intervalId);
            }
        }

        const intervalId = setInterval(checkInvoiceExpiry, 1000);
    }
});

const checkButtonCheckOutPage = document.querySelector('.checkButtonCheckOut');
if (checkButtonCheckOutPage) {
    function handlePaymentSelection() {
        const selectedPaymentMethod = document.querySelector('input[name="payment"]:checked');
        const invoiceId = document.querySelector('[data-invoice-id]').getAttribute('data-invoice-id');

        if (selectedPaymentMethod) {
            if (selectedPaymentMethod.id === 'cod') {
                $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    url: 'http://localhost:8080' + '/api/mangostore/cart/update-status?id=' + invoiceId,
                    dataType: 'json',
                    success: function (response) {
                        successAlert('Thanh toán hóa đơn thành công').then(response => {
                            if (response.value) {
                                window.location.href = `/mangostore/home`;
                            }
                        });
                    },
                    error: function (error) {
                        if (error.responseText === '1') {
                            dangerAlert('Số lượng trong sản phẩm không đủ, vui lòng quay lại giỏ hàng').then(response => {
                                if (response.value) {
                                    window.location.href = `/mangostore/cart`;
                                }
                            });
                        }
                    }
                });
            } else if (selectedPaymentMethod.id === 'banking') {
                window.location.href = `/mangostore/cart/banking-status?id=${invoiceId}`;
            }
        } else {
            dangerAlert('Vui lòng chọn một phương thức thanh toán');
        }
    }
}

const dataAddressClientPage = document.querySelector('.dataAddressClientPage');
if (dataAddressClientPage) {
    function submitAddressClient() {
        const form = document.getElementById("formAddressClientList");
        const isListEmpty = form.getAttribute("data-list-empty") === "true";

        if (isListEmpty) {
            dangerAlert("Vui lòng thêm địa chỉ");
            return;
        }

        const idCheckedAddress = document.querySelector('input[name="id"]:checked');
        if (!idCheckedAddress) {
            dangerAlert("Vui lòng chọn địa chỉ làm mặc định");
            return;
        }

        form.submit();
    }
}