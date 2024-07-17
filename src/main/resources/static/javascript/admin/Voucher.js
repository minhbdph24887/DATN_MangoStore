const addVoucherPage = document.querySelector('.addVoucherPage');
if (addVoucherPage) {
    function addVoucherForm() {
        const codeVoucher = document.getElementById('codeVoucherInput').value;
        const nameVoucher = document.getElementById('nameVoucherInput').value;
        const quantityVoucher = document.getElementById('quantityVoucherInput').value;
        const startDay = new Date(document.getElementById('startDay').value);
        const endDate = new Date(document.getElementById('endDate').value);

        let startDayVoucher = new Date(startDay);
        let endDateVoucher = new Date(endDate);
        const voucherPattern = /^[A-Z0-9]{10}$/;
        if (!voucherPattern.test(codeVoucher) && codeVoucher !== '') {
            dangerAlert('Lỗi định dạng mã Voucher');
        } else if (nameVoucher === '') {
            dangerAlert('Không được để trống tên Voucher');
        } else if (quantityVoucher === '') {
            dangerAlert('Không được để trống quantityVoucher');
        } else if (isNaN(quantityVoucher)) {
            dangerAlert('Bắt buộc phải là số');
        } else if (quantityVoucher <= 0) {
            dangerAlert('Số lượng không thể nhỏ hơn 0');
        } else if (!startDay || isNaN(startDayVoucher.getTime())) {
            dangerAlert('Ngày bắt đầu không hợp lệ');
        } else if (!endDate || isNaN(endDateVoucher.getTime())) {
            dangerAlert('Ngày kết thúc không hợp lệ');
        } else if (endDate <= new Date()) {
            dangerAlert('Ngày kết thúc không được trong quá khứ');
        } else if (endDate <= startDay) {
            dangerAlert('Ngày kết thúc không thể trước ngày bắt đầu');
        } else {
            const data = {
                codeVoucher: codeVoucher,
                nameVoucher: nameVoucher,
            }
            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080' + '/api/mangostore/admin/voucher/check-add',
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: 'application/json',
                success: function (response) {
                    const formVoucherAdd = document.getElementById('formVoucherAdd');
                    confirmAlertForm('Bạn có muốn thêm hay không', 'Thêm thành công', formVoucherAdd);
                },
                error: function (e) {
                    if (e.responseText === "1") {
                        dangerAlert('Mã Voucher Đã Tồn Tại');
                    } else if (e.responseText === '2') {
                        dangerAlert('Tên Voucher Đã Tồn Tại');
                    } else {
                        errorAlert('Lỗi.');
                    }
                    console.clear();
                }
            });
        }
    }
}

const updateVoucher = document.querySelector('.updateVoucherPage');
if (updateVoucher) {
    function updateVoucherForm() {
        const idVoucher = document.getElementById('idVoucher').value;
        const nameVoucher = document.getElementById('nameVoucherInput').value;
        const quantityVoucher = document.getElementById('quantityVoucherInput').value;
        const startDay = new Date(document.getElementById('startDay').value);
        const endDate = new Date(document.getElementById('endDate').value);
        const reducedValue = document.getElementById('reducedVoucherInput').value;
        const minimumInput = document.getElementById('minimumOrderInput').value;

        let startDayVoucher = new Date(startDay);
        let endDateVoucher = new Date(endDate);
        if (nameVoucher === '') {
            dangerAlert('Không được để trống Tên Voucher');
        } else if (quantityVoucher === '') {
            dangerAlert('Không được để trống Số Lượng Voucher');
        } else if (isNaN(quantityVoucher)) {
            dangerAlert('Bắt buộc phải là số');
        } else if (reducedValue === '') {
            dangerAlert('Không được để trống giá trị giảm Voucher');
        } else if (isNaN(reducedValue)) {
            dangerAlert('Reduce Voucher bat buoc phai la so');
        } else if (minimumInput === '') {
            dangerAlert('Không được để trống Minimum Voucher');
        } else if (isNaN(minimumInput)) {
            dangerAlert('Minimum Voucher bat buoc phai la so');
        } else if (!startDay || isNaN(startDayVoucher.getTime())) {
            dangerAlert('Ngày bắt đầu không hợp lệ');
        } else if (!endDate || isNaN(endDateVoucher.getTime())) {
            dangerAlert('Ngày kết thúc không hợp lệ');
        } else if (startDay <= new Date() && endDate <= new Date()) {
            dangerAlert('Ngày bắt đầu và ngày kết thúc không được trong quá khứ');
        } else if (endDate <= startDay) {
            dangerAlert('Ngày kết thúc không thể trước ngày bắt đầu !');
        } else {
            const data = {
                id: idVoucher,
                nameVoucher: nameVoucher,
            }
            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080' + '/api/mangostore/admin/voucher/check-update',
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: 'application/json',
                success: function (response) {
                    const formVoucher = document.getElementById('formVoucher');
                    confirmAlertForm('Bạn có muốn thêm hay không', 'them thanh cong', formVoucher);
                },
                error: function (e) {
                    if (e.responseText === "1") {
                        dangerAlert('Tên Voucher đã tồn tại');
                    } else {
                        errorAlert('Lỗi.');
                    }
                    console.clear();
                }
            });
        }
    }
}

const deleteVoucherPage = document.querySelector('.deleteVoucherPage');
if (deleteVoucherPage) {
    function removeVoucher(){
        const idVoucher = document.getElementById('idVoucher').value;
        const url = "http://localhost:8080/mangostore/admin/voucher/delete/" + idVoucher;
        confirmAlertLink(event, "Bạn có muốn xóa hay không ?", "Xóa thành công", url);
    }
}