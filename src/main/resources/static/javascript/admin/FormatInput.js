const checkInfoMoneyPage = document.querySelector('.checkInfoMoneyPage');
if (checkInfoMoneyPage) {
    document.addEventListener('DOMContentLoaded', function () {
        document.getElementById('saveButton').disabled = true;
    });

    function formatToCurrency(value) {
        const numberValue = parseFloat(value.replace(/[\D.]+/g, ''));
        return numberValue.toLocaleString('vi-VN');
    }

    function onInput(value) {
        const formattedValue = formatToCurrency(value);
        document.getElementById('currencyInput').value = formattedValue;
        const totalPayment = parseInt(document.getElementById('totalPayment').value.replace(/[\D.]+/g, ''));
        const clientMoney = parseInt(value.replace(/[\D.]+/g, ''));
        const remainingMoney = clientMoney - totalPayment;
        const messageElement = document.getElementById('remainingMoney');
        const saveButton = document.getElementById('saveButton');
        const outputElement = document.getElementById('output');
        const convertElement = document.getElementById('convert');

        if (clientMoney >= totalPayment) {
            saveButton.disabled = false;
            messageElement.innerText = (remainingMoney >= 0 ? formatToCurrency(remainingMoney.toString()) : '0') + ' VND';
            outputElement.value = remainingMoney;
            convertElement.value = clientMoney;

        } else {
            saveButton.disabled = true;
            messageElement.innerText = 'The amount the customer paid is not enough for the invoice amount';
            outputElement.value = '';
            convertElement.value = '';
        }
    }

    document.getElementById('currencyInput').addEventListener('input', function (event) {
        onInput(event.target.value);
    });

    document.getElementById('formatInput').addEventListener('submit', function (event) {
        event.preventDefault();
        onInput(document.getElementById('currencyInput').value);
    });

    function updateInvoiceStatus() {
        const idInvoice = document.getElementById("idInvoice").value;
        const returnClientMoney = parseInt(document.getElementById("convert").value);

        console.log(idInvoice);
        console.log(returnClientMoney);

        const data = {
            idInvoice: idInvoice,
            returnClientMoney: returnClientMoney,
        };

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "http://localhost:8080/api/mangostore/admin/sell/update-status",
            data: JSON.stringify(data),
            dataType: "json",
            success: function (responseData) {
                alert("Thanh Toan Thanh Cong");
                window.open("http://localhost:8080/mangostore/admin/sell", "_self");
            },
            error: function (e) {
                alert("Thanh Toan That Bai");
                console.log("ERROR: ", e);
            }
        });
    }
}
