const addVoucherPage = document.querySelector('.addVoucherPage');
if (addVoucherPage) {
    function addVoucherForm() {
        const codeVoucher = document.getElementById('codeVoucherInput').value;
        const nameVoucher = document.getElementById('nameVoucherInput').value;
        const voucherForm = document.getElementById('voucherForm').value;
        const rankVoucher = document.getElementById('rankVoucher').value;
        const quantityVoucher = document.getElementById('quantityVoucherInput').value;
        const minimumOrderValue = document.getElementById('minimumOrderValue').value;
        const reducedVoucherValue = document.getElementById('reducedVoucherValue').value;
        const startDay = new Date(document.getElementById('startDay').value);
        const endDate = new Date(document.getElementById('endDate').value);

        const today = new Date();
        today.setHours(0, 0, 0, 0);

        let startDayVoucher = new Date(startDay);
        let endDateVoucher = new Date(endDate);
        const voucherPattern = /^[A-Z0-9]{10}$/;
        if (!voucherPattern.test(codeVoucher) && codeVoucher !== '') {
            dangerAlert('Lỗi định dạng mã phiếu giảm giá');
        } else if (nameVoucher === '') {
            dangerAlert('Không được để trống tên phiếu giảm giá');
        } else if (quantityVoucher === '') {
            dangerAlert('Không được để trống số lượng phiếu giảm giá');
        }else if(voucherForm === ''){
            dangerAlert('Mời chọn hình thức phếu giảm giá');
        }else if(rankVoucher === ''){
            dangerAlert('Mời chọn hạng');
        } else if (isNaN(quantityVoucher)) {
            dangerAlert('Bắt buộc phải là số');
        } else if (quantityVoucher <= 0) {
            dangerAlert('Số lượng không thể nhỏ hơn 0');
        } else if (quantityVoucher > 1000) {
            dangerAlert('Bạn chỉ được tạo phiếu giảm giá tối đa 1000 lần sử dụng');
        } else if (minimumOrderValue === '') {
            dangerAlert('Giá tối thiểu đơn hàng không đươc để trống');
        } else if (reducedVoucherValue === '') {
            dangerAlert('Giá trị giảm không được để trống');
        } else if (parseInt(reducedVoucherValue) > parseInt(minimumOrderValue) * 0.4) {
            dangerAlert('Giá trị giảm không được vượt quá 40% giá trị đơn hàng tối thiểu');
        } else if (!startDay || isNaN(startDayVoucher.getTime())) {
            dangerAlert('Ngày bắt đầu không hợp lệ');
        } else if (!endDate || isNaN(endDateVoucher.getTime())) {
            dangerAlert('Ngày kết thúc không hợp lệ');
        } else if (endDate < today) {
            dangerAlert('Ngày kết thúc không được trong quá khứ');
        } else if (endDate < startDay) {
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
                        dangerAlert('Mã phiếu giảm giá đã tồn tại');
                    } else if (e.responseText === '2') {
                        dangerAlert('Tên phiếu giảm giá đã tồn tại');
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
        const voucherForm = document.getElementById('voucherForm').value;
        const rankVoucher = document.getElementById('rankVoucher').value;
        const quantityVoucher = document.getElementById('quantityVoucherInput').value;
        const minimumOrderValue = document.getElementById('minimumOrderValue').value;
        const reducedVoucherValue = document.getElementById('reducedVoucherValue').value;
        const startDay = new Date(document.getElementById('startDay').value);
        const endDate = new Date(document.getElementById('endDate').value);

        const today = new Date();
        today.setHours(0, 0, 0, 0);

        let startDayVoucher = new Date(startDay);
        let endDateVoucher = new Date(endDate);
        if (nameVoucher === '') {
            dangerAlert('Không được để trống tên phiếu giảm giá');
        } else if (quantityVoucher === '') {
            dangerAlert('Không được để trống số lượng phiếu giảm giá');
        }else if(voucherForm === ''){
            dangerAlert('Mời chọn hình thức phếu giảm giá');
        }else if(rankVoucher === ''){
            dangerAlert('Mời chọn hạng');
        } else if (isNaN(quantityVoucher)) {
            dangerAlert('Bắt buộc phải là số');
        } else if (quantityVoucher <= 0) {
            dangerAlert('Số lượng không thể nhỏ hơn 0');
        } else if (quantityVoucher > 1000) {
            dangerAlert('Bạn chỉ được cập nhật phiếu giảm giá tối đa 1000 lần sử dụng');
        } else if (minimumOrderValue === '') {
            dangerAlert('Giá tối thiểu đơn hàng không đươc để trống');
        } else if (reducedVoucherValue === '') {
            dangerAlert('Giá trị giảm không được để trống');
        } else if (parseInt(reducedVoucherValue) > parseInt(minimumOrderValue) * 0.4) {
            dangerAlert('Giá trị giảm không được vượt quá 40% giá trị đơn hàng tối thiểu');
        } else if (!startDay || isNaN(startDayVoucher.getTime())) {
            dangerAlert('Ngày bắt đầu không hợp lệ');
        } else if (!endDate || isNaN(endDateVoucher.getTime())) {
            dangerAlert('Ngày kết thúc không hợp lệ');
        } else if (endDate < today) {
            dangerAlert('Ngày kết thúc không được trong quá khứ');
        } else if (endDate < startDay) {
            dangerAlert('Ngày kết thúc không thể trước ngày bắt đầu');
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
                    confirmAlertForm('Bạn có muốn cập nhật hay không', 'Cập nhật thành công', formVoucher);
                },
                error: function (e) {
                    if (e.responseText === "1") {
                        dangerAlert('Tên phiếu giảm giá đã tồn tại');
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
    function removeVoucher() {
        const idVoucher = document.getElementById('idVoucher').value;
        const url = "http://localhost:8080/mangostore/admin/voucher/delete/" + idVoucher;
        confirmAlertLink(event, "Bạn có muốn xóa hay không ?", "Xóa thành công", url);
    }
}