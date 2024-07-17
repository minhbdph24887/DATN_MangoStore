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
                    dangerAlert('The invoice has expired, please create a new invoice.').then((result) => {
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
                        successAlert('Order Success').then(response => {
                            if (response.value) {
                                window.location.href = `/mangostore/home`;
                            }
                        });
                    },
                    error: function (error) {
                        if (error.responseText === '1') {
                            dangerAlert('The quantity in the product is not enough, please return to the cart').then(response => {
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
            dangerAlert('Please select a payment method');
        }
    }
}