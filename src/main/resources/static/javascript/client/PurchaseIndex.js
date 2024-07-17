const indexPurchasePage = document.querySelector(".IndexPurchasePage");
if (indexPurchasePage) {
    function changeStatus(status) {
        window.location.href = 'http://localhost:8080/mangostore/purchase?status=' + status;
    }
}

const detailPurchasePage = document.querySelector(".DetailPurchasePage");
if (detailPurchasePage) {
    document.addEventListener("DOMContentLoaded", function () {
        const buttons = document.querySelectorAll("[data-bs-target='#myModal']");
        buttons.forEach(button => {
            button.addEventListener("click", function () {
                const invoiceId = this.getAttribute("data-id");
                fetch(`/api/mangostore/invoice/${invoiceId}`)
                    .then(response => response.json())
                    .then(data => {
                        document.getElementById("order-id").textContent = "Order ID: " + data.codeInvoice;
                        document.getElementById("invoice-payment-date").textContent = data.invoicePaymentDate;
                        document.getElementById("customer-address").textContent = data.customerAddress;
                        document.getElementById("total-payment").textContent = data.totalPayment;
                        document.getElementById("id-invoice").textContent = data.id;

                        const status = data.invoiceStatus;
                        const track = document.getElementById("track");
                        const cancelInvoice = document.getElementById('cancel-invoice');
                        if (status === 6) {
                            track.style.display = "none";
                        } else {
                            track.style.display = "flex";
                            for (let i = 1; i <= 5; i++) {
                                const step = document.getElementById(`step${i}`);
                                if (status >= i) {
                                    step.classList.add("active");
                                } else {
                                    step.classList.remove("active");
                                }
                            }
                        }

                        if (status >= 3) {
                            cancelInvoice.style.display = "none";
                        } else {
                            cancelInvoice.style.display = "block";
                        }

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

const cancelPurchasePage = document.querySelector(".CancelPurchasePage");
if (cancelPurchasePage) {
    function cancelInvoice() {
        const idInvoice = document.getElementById('id-invoice').textContent;
        const url = 'http://localhost:8080/mangostore/purchase/remove?id=' + idInvoice;
        confirmAlertLink('You Want To Cancel Your Invoice?', 'Cancel Successfully', url);
    }
}