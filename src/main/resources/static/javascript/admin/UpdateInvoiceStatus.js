const updateInvoiceStatusPage = document.querySelector(".updateInvoiceStatusPage");
if (updateInvoiceStatusPage) {
    function updateStatusInvoice(event) {
        const idInvoice = event.target.getAttribute('data-id');
        window.location.href = 'http://localhost:8080/mangostore/admin/order/update-invoice-status/' + idInvoice;
    }
}