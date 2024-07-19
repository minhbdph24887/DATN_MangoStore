const checkNumberPhone = document.querySelector('.checkNumberPhonePage');
if (checkNumberPhone) {
    $(document).ready(function () {
        const $select2 = $('.select2-search-example').select2({
            placeholder: "NumberClient",
            allowClear: true
        });

        $select2.on('select2:select', function (e) {
            var data = e.params.data;
            if (data.id === 'add') {
                $('#addClient').modal('show');
                $select2.val(null).trigger('change');
            }
        });
    });

    function updateClient() {
        const idInvoice = document.getElementById('idInvoiceInputNumber').value;
        const optionClientSelect = document.getElementById("optionClient");
        const selectedOptionValue = optionClientSelect.value;
        const data = {
            idInvoice: idInvoice,
            idClient: selectedOptionValue,
        }

        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080' + '/api/mangostore/admin/sell/add-client',
            data: JSON.stringify(data),
            contentType: 'application/json',
            dataType: 'json',
            success: function (response) {
                window.location.href = 'http://localhost:8080/mangostore/admin/sell/edit?id=' + idInvoice;
            },
            error: function (e) {
                if (e.responseText === '1') {
                    dangerAlert('You cannot create an invoice with yourself as a customer')
                } else {
                    errorAlert('An error occurred');
                    console.log(e);
                }
            }
        });

    }


    function addClient() {
        const nameClient = document.getElementById('nameClientInput').value;
        const numberPhone = document.getElementById('numberClientInput').value;
        const emailClient = document.getElementById('emailClientInput').value;
        const idInvoice = document.getElementById('idInvoiceInputNumber').value;

        const phoneRegex = /^[0-9]{10}$/;
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (nameClient === '') {
            dangerAlert('Please Enter Name Client');
        } else if (numberPhone === '') {
            dangerAlert('Please Enter Number Phone');
        } else if (!phoneRegex.test(numberPhone)) {
            dangerAlert('Number Phone Format Is Incorrect');
        } else if (emailClient === '') {
            dangerAlert('Please Enter Email');
        } else if (!emailRegex.test(emailClient)) {
            dangerAlert('Email Format Is Incorrect');
        } else {
            const data = {
                nameClient: nameClient,
                numberPhone: numberPhone,
                email: emailClient,
            }

            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080' + '/api/mangostore/admin/sell/add-new-client',
                data: JSON.stringify(data),
                contentType: 'application/json',
                dataType: 'json',
                success: function (response) {
                    window.location.href = 'http://localhost:8080/mangostore/admin/sell/edit?id=' + idInvoice;
                },
                error: function (e) {
                    if (e.responseText === '1') {
                        dangerAlert('Number Phone already exists');
                    } else if (e.responseText === '2') {
                        dangerAlert('Email already exists');
                    } else {
                        errorAlert('An error occurred');
                        console.log(e);
                    }
                }
            });
        }
    }

    function clearFrom() {
        const nameClientInput = document.getElementById('nameClientInput');
        const numberPhoneInput = document.getElementById('numberClientInput');
        const emailClientInput = document.getElementById('emailClientInput');
        nameClientInput.value = '';
        numberPhoneInput.value = '';
        emailClientInput.value = '';
    }
}

const checkVoucher = document.querySelector('.checkVoucherPage');
if (checkVoucher) {
    function updateVoucherForSell() {
        const voucherSelect = document.getElementById('voucherSelect');
        const selectedValue = voucherSelect.value;

        if (selectedValue === "") {
            dangerAlert("Please Choose The Voucher");
        } else {
            const idInvoice = document.getElementById('idInvoiceInput').value;
            const data = {
                idInvoice: idInvoice,
                idVoucher: selectedValue,
            }
            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080' + '/api/mangostore/admin/sell/add-voucher',
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: 'application/json',
                success: function (response) {
                    window.location.href = 'http://localhost:8080/mangostore/admin/sell/edit?id=' + idInvoice;
                },
                error: function (e) {
                    if (e.responseText === "1") {
                        dangerAlert('Please select a product to use the voucher');
                    } else if (e.responseText === "2") {
                        dangerAlert('This voucher cannot be used with this receipt');
                    }
                    console.clear();
                }
            });
        }
    }
}

const addProductDetailPage = document.querySelector('.addProductDetail');
if (addProductDetailPage) {
    function AddProductDetail(button) {
        const idProductDetail = button.getAttribute('data-id');
        const quantityNew = document.getElementById('quantityNewInput' + idProductDetail).value;
        if (quantityNew <= 0) {
            dangerAlert('quantity does not exist, please re-enter');
        }else if(quantityNew > 50){
            dangerAlert('You can only purchase a maximum of 50 products/items');
        } else {
            const idInvoice = document.getElementById('idInvoiceForAddProduct' + idProductDetail).value;
            const idProduct = document.getElementById('idProductDetailForSell' + idProductDetail).value;
            const data = {
                idInvoice: idInvoice,
                idProductDetail: idProduct,
                newQuantity: quantityNew,
            }
            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080' + '/api/mangostore/admin/sell/add-product',
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: 'application/json',
                success: function (response) {
                    successAlert('Add Product Successfully');
                },
                error: function (e) {
                    if (e.responseText === '1') {
                        dangerAlert('Insufficient quantity');
                    }
                    console.clear();
                }
            });
        }
    }
}

const updateQuantityInvoiceDetailPage = document.querySelector('.updateQuantityInvoiceDetail');
if (updateQuantityInvoiceDetailPage) {
    function reduceQuantity(idInvoiceDetail) {
        const data = {
            idInvoiceDetail: idInvoiceDetail,
        }

        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080' + '/api/mangostore/admin/sell/reduce',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json',
            success: function (response) {
                window.location.reload();
            },
            error: function (e) {
                console.clear();
            }
        });
    }

    function increaseQuantity(idInvoiceDetail) {
        const data = {
            idInvoiceDetail: idInvoiceDetail,
        }

        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080' + '/api/mangostore/admin/sell/increase',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json',
            success: function (response) {
                window.location.reload();
            },
            error: function (e) {
                if (e.responseText === '1') {
                    dangerAlert('The quantity added must not exceed the quantity in stock');
                }else if(e.responseText === '2'){
                    dangerAlert('You can only purchase a maximum of 50 products/items');
                }
                console.clear();
            }
        });
    }
}

const updateStatusInvoicePage = document.querySelector('.updateStatusInvoice');
if (updateStatusInvoicePage) {
    function updateInvoiceStatusSell() {
        const idInvoice = document.getElementById("idInvoice").value;
        const returnClientMoney = parseInt(document.getElementById("convert").value);

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
                successAlert('Payment success').then(rest => {
                    if (rest.value) {
                        window.open("http://localhost:8080/mangostore/admin/sell", "_self");
                    }
                });
            },
            error: function (e) {
                if (e.responseText === '1') {
                    dangerAlert('The quantity of a certain product has run out')
                } else if (e.responseText === '2') {
                    dangerAlert('An error occurred while exporting the pdf invoice');
                } else {
                    errorAlert('Payment failed');
                }
                console.log(e)
            }
        });
    }
}