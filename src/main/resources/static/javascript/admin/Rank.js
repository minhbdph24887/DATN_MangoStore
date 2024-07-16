const updateRank = document.querySelector('.updateRank');
if(updateRank){
    function updateRankForm(){
        const nameRank = document.getElementById('nameRankInput').value;
        const minimumScore = document.getElementById('minimumScore').value;
        const maximumScore = document.getElementById('maximumScore').value;
        if (nameRank === ''){
            dangerAlert('Không được để trống tên rank');
        }else if (minimumScore <= 0){
            dangerAlert('Điểm tối thiểu không thể nhỏ hơn hoặc bằng 0 !');
        }else if (maximumScore <= 0){
            dangerAlert('Điểm tối đa không thể nhỏ hơn hoặc bằng 0 !');
        }else if (maximumScore <= minimumScore){
            dangerAlert('Điểm tối đa phải lớn hơn tối thiểu !');
        }else {
            const formVoucher = document.getElementById('formRank');
            confirmAlertForm('Bạn có muốn cập nhật hay không','them thanh cong',formVoucher);
        }
    }
}

const addRank = document.querySelector('.addRank');
if(addRank){
    function addRankForm(){
        const nameRank = document.getElementById('nameRankInput').value;
        const minimumScore = document.getElementById('minimumScore').value;
        const maximumScore = document.getElementById('maximumScore').value;
        if (nameRank === ''){
            dangerAlert('Không được để trống tên rank');
        }else if (minimumScore <= 0){
            dangerAlert('Điểm tối thiểu không thể nhỏ hơn hoặc bằng 0 !');
        }else if (maximumScore <= 0){
            dangerAlert('Điểm tối đa không thể nhỏ hơn hoặc bằng 0 !');
        }else if (maximumScore <= minimumScore){
            dangerAlert('Điểm tối đa phải lớn hơn tối thiểu !');
        }else {
            const formVoucher = document.getElementById('formRank');
            confirmAlertForm('Bạn có muốn thêm hay không','them thanh cong',formVoucher);
        }
    }
}