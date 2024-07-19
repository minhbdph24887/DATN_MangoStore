const formatMoneyPage = document.querySelector(".indexFormatMoneyPage");
if (formatMoneyPage) {
    function formatToCurrencyAll(value) {
        const numberValue = parseInt(value.replace(/[\D.]+/g, ''));
        return numberValue.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'}).slice(0, -2);
    }

    function formatToNumberAll(value) {
        return value.replace(/[\D.]+/g, '');
    }

    function onInputAll(value) {
        document.getElementById('currencyInput').value = formatToCurrencyAll(value);
        document.getElementById('output').value = formatToNumberAll(formatToCurrencyAll(value));
    }
}

const formatMoneyImportPriceProductDetail = document.querySelector(".formatMoneyImportPriceProductDetail");
if (formatMoneyImportPriceProductDetail) {
    function formatToCurrencyPDImport(value) {
        const numberValue = parseInt(value.replace(/[\D.]+/g, ''));
        return numberValue.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'}).slice(0, -2);
    }

    function formatToNumberPDImport(value) {
        return value.replace(/[\D.]+/g, '');
    }

    function onInputImportPriceProductDetail(value, id) {
        document.getElementById(id).value = formatToCurrencyPDImport(value);
        document.getElementById('outputImportPrice' + id.slice(-2)).value = formatToNumberPDImport(formatToCurrencyPDImport(value));
    }
}

const formatMoneyPriceProductDetail = document.querySelector(".formatMoneyPriceProductDetail");
if (formatMoneyPriceProductDetail) {
    function formatToCurrencyPDOut(value) {
        const numberValue = parseInt(value.replace(/[\D.]+/g, ''));
        return numberValue.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'}).slice(0, -2);
    }

    function formatToNumberPDOut(value) {
        return value.replace(/[\D.]+/g, '');
    }

    function onInputPricePDOut(value, id) {
        document.getElementById(id).value = formatToCurrencyPDOut(value);
        document.getElementById('outputPrice' + id.slice(-2)).value = formatToNumberPDOut(formatToCurrencyPDOut(value));
    }
}

const formatMoneyFrom = document.querySelector(".fromFormatMoney");
if (formatMoneyFrom) {
    function formatCurrencyOnLoadAll() {
        const value = document.getElementById('currencyInput').value;
        document.getElementById('currencyInput').value = formatToCurrency(value);
    }

    function prepareValueForSubmitAll() {
        const value = document.getElementById('currencyInput').value;
        document.getElementById('output').value = formatToNumberVD(value);
        return true;
    }

    function formatToCurrencyAllVD(value) {
        const numberValue = parseInt(value.replace(/[\D.]+/g, ''));
        return numberValue.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'}).slice(0, -2);
    }

    function formatToNumberVD(value) {
        return value.replace(/[\D.]+/g, '');
    }

    function onInput(value) {
        document.getElementById('currencyInput').value = formatToCurrencyAllVD(value);
    }

    window.onload = formatCurrencyOnLoadAll;

}

const formEditProductDetail = document.querySelector(".formEditProductDetail");

if (formEditProductDetail) {
    function formatCurrencyOnLoadPDD() {
        const valueImportPrice = document.getElementById('importPriceInput').value;
        const valuePrice = document.getElementById('priceInput').value;
        document.getElementById('importPriceInput').value = formatToCurrencyPDD(valueImportPrice);
        document.getElementById('priceInput').value = formatToCurrencyPDD(valuePrice);
    }

    function prepareValueForSubmitPDD() {
        const valueImportPrice = document.getElementById('importPriceInput').value;
        const valuePrice = document.getElementById('priceInput').value;
        document.getElementById('outputImportPrice').value = removeCommasPDD(valueImportPrice);
        document.getElementById('outputPrice').value = removeCommasPDD(valuePrice);

        const activeCheckbox = document.getElementById("active");
        const statusInput = document.getElementById("status");
        statusInput.value = activeCheckbox.checked ? 1 : 0;

        return true;
    }
// Hàm để loại bỏ ký tự không phải số
    function removeNonNumeric(value) {
        return value.replace(/[^0-9]/g, '');
    }
    function onInputImportPrice(value) {
        // Format giá cho người dùng xem
        const formattedValue = formatToCurrencyPDD(value);
        document.getElementById('importPriceInput').value = formattedValue;
        // Lưu giá gốc vào hidden input
        document.getElementById('outputImportPrice').value = removeNonNumeric(value);
    }

    function onInputPrice(value) {
        // Format giá cho người dùng xem
        const formattedValue = formatToCurrencyPDD(value);
        document.getElementById('priceInput').value = formattedValue;
        // Lưu giá gốc vào hidden input
        document.getElementById('outputPrice').value = removeNonNumeric(value);
    }

    function formatToCurrencyPDD(value) {
        value = value.replace(/\D/g, '');
        return new Intl.NumberFormat('en-US').format(value);
    }

    function removeCommasPDD(value) {
        return value.replace(/,/g, '');
    }

    formatCurrencyOnLoadPDD();
}
