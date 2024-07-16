const addVoucher = document.querySelector('.addVoucher');
if(addVoucher){
    function addVoucherForm(){
        const codeVoucher = document.getElementById('codeVoucherInput').value;
        const nameVoucher = document.getElementById('nameVoucherInput').value;
        const quantityVoucher = document.getElementById('quantityVoucherInput').value;
        const startDay = new Date(document.getElementById('startDay').value);
        const endDate = new Date(document.getElementById('endDate').value);

        let startDayVoucher = new Date(startDay);
        let endDateVoucher = new Date(endDate);
        const voucherPattern = /^[A-Z0-9]{10}$/;
        if (!voucherPattern.test(codeVoucher) && codeVoucher !== ''){
            dangerAlert('looi nhap code voucher');
        }else if (nameVoucher === ''){
            dangerAlert('Không được để trống nameVoucher');
        }else if (quantityVoucher === ''){
            dangerAlert('Không được để trống quantityVoucher');
        }else if (isNaN(quantityVoucher)){
            dangerAlert('bat buoc phai la so');
        }else if (quantityVoucher <= 0){
            dangerAlert('so luong ko the nho hon 0');
        }else if (!startDay || isNaN(startDayVoucher.getTime())) {
            dangerAlert('Ngày bắt đầu không hợp lệ');
        }else if (!endDate || isNaN(endDateVoucher.getTime())) {
            dangerAlert('Ngày kết thúc không hợp lệ');
        }else if (startDay <= new Date() && endDate <= new Date() ) {
            dangerAlert('Ngày bắt đầu và ngày kết thúc không được trong quá khứ');
        }else if (endDate <= startDay){
            dangerAlert('Ngày kết thúc không thể trước ngày bắt đầu !');
        }else {
            const formVoucher = document.getElementById('formVoucher');
            confirmAlertForm('Bạn có muốn thêm hay không','them thanh cong',formVoucher);
        }
    }
}


const updateVoucher = document.querySelector('.updateVoucher');
if(updateVoucher){
    function updateVoucherForm(){
        const nameVoucher = document.getElementById('nameVoucherInput').value;
        const quantityVoucher = document.getElementById('quantityVoucherInput').value;
        const startDay = new Date(document.getElementById('startDay').value);
        const endDate = new Date(document.getElementById('endDate').value);
        const reducedValue = document.getElementById('reducedVoucherInput').value;
        const minimumInput = document.getElementById('minimumOrderInput').value;

        let startDayVoucher = new Date(startDay);
        let endDateVoucher = new Date(endDate);
        if (nameVoucher === ''){
            dangerAlert('Không được để trống nameVoucher');
        }else if (quantityVoucher === ''){
            dangerAlert('Không được để trống quantityVoucher');
        }else if (isNaN(quantityVoucher)){
            dangerAlert('bat buoc phai la so');
        }else if (reducedValue === ''){
            dangerAlert('Không được để trống Reduce Voucher');
        }else if (isNaN(reducedValue)){
            dangerAlert('Reduce Voucher bat buoc phai la so');
        }else if (minimumInput === ''){
            dangerAlert('Không được để trống Minimum Voucher');
        }else if (isNaN(minimumInput)){
            dangerAlert('Minimum Voucher bat buoc phai la so');
        }else if (!startDay || isNaN(startDayVoucher.getTime())) {
            dangerAlert('Ngày bắt đầu không hợp lệ');
        }else if (!endDate || isNaN(endDateVoucher.getTime())) {
            dangerAlert('Ngày kết thúc không hợp lệ');
        }else if (startDay <= new Date() && endDate <= new Date() ) {
            dangerAlert('Ngày bắt đầu và ngày kết thúc không được trong quá khứ');
        }else if (endDate <= startDay){
            dangerAlert('Ngày kết thúc không thể trước ngày buắt đầu !');
        }else {
            const formVoucher = document.getElementById('formVoucher');
            confirmAlertForm('Bạn có muốn thêm hay không','them thanh cong',formVoucher);
        }
    }
}