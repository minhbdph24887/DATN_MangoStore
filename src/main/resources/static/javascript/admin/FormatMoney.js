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
        document.getElementById('outputImportPrice').value = formatToNumberPDD(valueImportPrice);
        document.getElementById('outputPrice').value = formatToNumberPDD(valuePrice);
        return true;
    }

    function formatToCurrencyPDD(value) {
        const numberValue = parseInt(value.replace(/[\D.]+/g, ''));
        return numberValue.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'}).slice(0, -2);
    }

    function formatToNumberPDD(value) {
        return value.replace(/[\D.]+/g, '');
    }

    function onInputImportPrice(value) {
        document.getElementById('importPriceInput').value = formatToCurrencyPDD(value);
        document.getElementById('outputImportPrice').value = formatToNumberPDD(formatToCurrencyPDD(value));
    }

    function onInputPrice(value) {
        document.getElementById('priceInput').value = formatToCurrencyPDD(value);
        document.getElementById('outputPrice').value = formatToNumberPDD(formatToCurrencyPDD(value));
    }

    window.onload = formatCurrencyOnLoadPDD;

}