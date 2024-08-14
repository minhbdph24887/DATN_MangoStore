const checkNumberPhone = document.querySelector('.checkNumberPhonePage');
if (checkNumberPhone) {
    $(document).ready(function () {
        const $select2 = $('.select2-search-example').select2({
            placeholder: "Số điện thoại khách hàng",
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
        if (selectedOptionValue === '') {
            dangerAlert('Vui lòng thêm khách hàng vào hoá đơn');
        } else {
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
                        dangerAlert('Bạn không thể tạo hóa đơn với tư cách là khách hàng')
                    } else {
                        errorAlert('An error occurred');
                    }
                }
            });
        }
    }


    function addClient() {
        const nameClient = document.getElementById('nameClientInput').value;
        const numberPhone = document.getElementById('numberClientInput').value;
        const emailClient = document.getElementById('emailClientInput').value;
        const idInvoice = document.getElementById('idInvoiceInputNumber').value;

        const phoneRegex = /^[0-9]{10}$/;
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (nameClient === '') {
            dangerAlert('Tên khách hàng không được để trống ');
        } else if (numberPhone === '') {
            dangerAlert('Số điện thoại khách hàng không được để trống');
        } else if (!phoneRegex.test(numberPhone)) {
            dangerAlert('Số điện thoại không đúng định dạng');
        } else if (emailClient === '') {
            dangerAlert('Vui lòng nhập email');
        } else if (!emailRegex.test(emailClient)) {
            dangerAlert('Email không đúng định dạng');
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
                        dangerAlert('Số điện thoại đã tồn tại');
                    } else if (e.responseText === '2') {
                        dangerAlert('Email đã tồn tại');
                    } else {
                        errorAlert('Lỗi');
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

const comboboxVoucherPage = document.querySelector('.comboboxVoucherPage');
if (comboboxVoucherPage) {
    function showDropdown() {
        document.getElementById('comboboxDropdown').classList.add('show');
    }

    function hideDropdown() {
        setTimeout(function () {
            document.getElementById('comboboxDropdown').classList.remove('show');
        }, 200);
    }
}

const checkVoucher = document.querySelector('.checkVoucherPage');
if (checkVoucher) {
    function updateVoucherForSell() {
        const codeVoucher = document.getElementById('codeVoucherInput').value;
        const idInvoice = document.getElementById('idInvoiceInput').value;
        const totalInvoiceAmount = parseFloat(document.getElementById('totalInvoiceAmount').value.replace(/,/g, ''));
        if (codeVoucher === '') {
            const checkInputVoucherList = document.querySelector('input[name="idVoucher"]:checked');
            if (checkInputVoucherList) {
                const id = checkInputVoucherList.value;
                const minimumPrice = parseFloat(document.getElementById('minimumPrice' + id).value.replace(/,/g, ''));
                if (isNaN(totalInvoiceAmount)) {
                    dangerAlert('Vui lòng chọn sản phẩm để sử dụng phiếu giảm giá');
                } else if (totalInvoiceAmount < minimumPrice) {
                    dangerAlert('Phiếu giảm giá này không thể được sử dụng với hóa đơn này');
                } else {
                    const data = {
                        idInvoice: idInvoice,
                        idVoucherCombobox: id,
                    }
                    const url = 'http://localhost:8080/api/mangostore/admin/sell/add-voucher-list';
                    addVoucherAPI(url, data);
                }
            } else {
                dangerAlert('Vui lòng chọn phiếu giảm giá');
            }
        } else {
            const voucherPattern = /^[A-Z0-9]{10}$/;
            if (!voucherPattern.test(codeVoucher)) {
                dangerAlert('Mã phiếu giảm giá không hợp lệ');
            } else if (isNaN(totalInvoiceAmount)) {
                dangerAlert('Vui lòng thêm sản phẩm để sử dụng phiếu giảm giá');
            } else {
                const idCustomer = document.getElementById('idCustomer').value;
                const data = {
                    idInvoice: idInvoice,
                    idCustomer: idCustomer,
                    codeVoucher: codeVoucher,
                    totalPayment: totalInvoiceAmount,
                }
                const url = 'http://localhost:8080/api/mangostore/admin/sell/add-voucher-code';
                addVoucherAPI(url, data);
            }
        }

        function addVoucherAPI(url, data) {
            $.ajax({
                type: 'POST',
                url: url,
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: 'application/json',
                success: function (response) {
                    window.location.href = 'http://localhost:8080/mangostore/admin/sell/edit?id=' + data.idInvoice;
                },
                error: function (e) {
                    if (e.responseText === "1") {
                        dangerAlert('Hóa đơn không tồn tại');
                    } else if (e.responseText === "2") {
                        dangerAlert('Phiếu giảm giá không tồn tại');
                    } else if (e.responseText === "3") {
                        dangerAlert('Phiếu giảm này này đã hết vui lòng chọn phiếu giảm giá khác');
                    } else if (e.responseText === "4") {
                        dangerAlert('Vui lòng thêm thông tin vào hóa đơn');
                    } else if (e.responseText === "5") {
                        dangerAlert('Phiếu giảm giá không tồn tại');
                    } else if (e.responseText === "6") {
                        dangerAlert('Phiếu giảm giá này không thể sử dụng với hóa đơn này');
                    } else if (e.responseText === "7") {
                        dangerAlert('Mức rank của bạn không thể sử dụng phiếu giảm giá này');
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
            dangerAlert('Số lượng không hợp lệ, vui lòng nhập lại');
        } else if (quantityNew > 50) {
            dangerAlert('Bạn chỉ có thể mua tối đa 50 sản phẩm/món hàng');
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
                    successAlert('Thêm sản phẩm thành công');
                },
                error: function (e) {
                    if (e.responseText === '1') {
                        dangerAlert('Không đủ số lượng');
                    } else if (e.responseText === '2') {
                        dangerAlert('bạn đã thêm vượt quá 50 sản phẩm/món hàng rồi');
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
                    dangerAlert('Số lượng thêm vào không được vượt quá số lượng tồn');
                } else if (e.responseText === '2') {
                    dangerAlert('Bạn chỉ có thể mua tối đa 50 sản phẩm/món hàng');
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
                successAlert('Thanh toán thành công').then(rest => {
                    if (rest.value) {
                        window.open("http://localhost:8080/mangostore/admin/sell", "_self");
                    }
                });
            },
            error: function (e) {
                if (e.responseText === '1') {
                    dangerAlert('Số lượng của một sản phẩm nhất định đã hết')
                } else if (e.responseText === '2') {
                    dangerAlert('Đã xảy ra lỗi khi xuất hóa đơn pdf');
                } else {
                    errorAlert('Thanh toán thật bại');
                }
            }
        });
    }
}