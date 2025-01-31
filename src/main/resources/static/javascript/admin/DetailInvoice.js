const detailInvoiceDetails = document.querySelector('.DetailInvoiceOnlinePage');
if (detailInvoiceDetails) {
    document.addEventListener('DOMContentLoaded', function () {
        const buttons = document.querySelectorAll("[data-target='#detailInvoice']");
        buttons.forEach(button => {
            button.addEventListener('click', function () {
                const invoiceId = this.getAttribute('data-id');
                fetch(`/api/mangostore/admin/invoice/${invoiceId}`)
                    .then(response => response.json())
                    .then(data => {
                        document.getElementById('order-id').textContent = "Mã hóa đơn: " + data.codeInvoice;
                        document.getElementById('invoice-payment-date').textContent = data.invoicePaymentDate;
                        document.getElementById('customer-address').textContent = data.customerAddress;
                        document.getElementById('total-payment').textContent = data.totalPayment;
                        document.getElementById("id-invoice").textContent = data.id;
                        document.getElementById('name-customer').textContent = "Tên khách hàng: " + data.nameCustomer;

                        const invoiceItems = document.getElementById("invoice-items");
                        invoiceItems.innerHTML = '';
                        data.invoiceDetails.forEach(item => {
                            const li = document.createElement("li");
                            li.className = "col-md-12";
                            li.innerHTML = `
                                <figure class="itemside mb-3">
                                    <div class="row">
                                        <div class="col-md-2">
                                            <div class="aside">
                                                <img src="/images/product/${item.imageProduct}" class="img-sm border" alt="">
                                            </div>
                                        </div>
                                        <div class="col-md-9">
                                            <figcaption>
                                                <p style="margin-top: 0; margin-bottom: 1rem; font-size: 30px;">${item.productName}</p>
                                                <span class="text-muted">Size ${item.nameSize}, Color ${item.nameColor} x ${item.quantity}</span> <br>
                                                <span class="text-muted">${item.price}</span>
                                            </figcaption>
                                        </div>
                                    </div>
                                </figure>
                            `;
                            invoiceItems.appendChild(li);
                        });
                    });
            });
        });
    });
}

const ConfirmInvoicePage = document.querySelector('.ConfirmInvoicePage');
if (ConfirmInvoicePage) {
    function confirmInvoice() {
        const idInvoice = document.getElementById('id-invoice').textContent;
        const data = {
            idInvoice: idInvoice,
        }
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080' + '/api/mangostore/admin/check-confirm-invoice',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json',
            success: function (response) {
                const url = 'http://localhost:8080/mangostore/admin/confirm-invoice?id=' + idInvoice;
                confirmAlertLink(event, 'Bạn có muốn tiếp nhận hóa đơn này ?', 'Tiếp nhận thành công', url);
            },
            error: function (response) {
                if (response.responseText === '1') {
                    dangerAlert('Bạn không thể xác nhận hóa đơn của chính mình!');
                } else if (response.responseText === '2') {
                    dangerAlert('Số lượng trong sản phẩm không đủ');
                } else {
                    dangerAlert('Lỗi');
                }
            }
        });
    }
}

const CancelInvoiceForAdminPage = document.querySelector('.CancelInvoiceForAdminPage');
if (CancelInvoiceForAdminPage) {
    function cancelInvoiceForAdmin() {
        const idInvoice = document.getElementById('id-invoice').textContent;
        const url = 'http://localhost:8080/mangostore/purchase/remove?id=' + idInvoice;
        confirmAlertLink(event,'Bạn muốn hủy hóa đơn này ?', 'Hủy thành công', url);
    }
}