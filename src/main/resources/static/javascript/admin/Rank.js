const updateRank = document.querySelector('.updateRankPage');
if (updateRank) {
    function updateRankForm() {
        const id = document.getElementById('idRank').value;
        const nameRank = document.getElementById('nameRankInput').value;
        const minimumScore = document.getElementById('minimumScore').value;
        const maximumScore = document.getElementById('maximumScore').value;
        if (nameRank === '') {
            dangerAlert('Tên hạng không được để trống');
        } else if (minimumScore <= 0) {
            dangerAlert('Điểm tối thiểu không thể nhỏ hơn hoặc bằng 0');
        } else if (maximumScore <= 0) {
            dangerAlert('Điểm tối đa không thể nhỏ hơn hoặc bằng 0');
        } else if (minimumScore >= maximumScore) {
            dangerAlert('Điểm tối đa phải lớn hơn điểm tối thiểu');
        } else {
            const data = {
                id: id,
                nameRank: nameRank,
            }
            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080' + '/api/mangostore/admin/rank/check-update',
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: 'application/json',
                success: function (response) {
                    const formRankUpdate = document.getElementById('formRankUpdate');
                    confirmAlertForm('Bạn muốn cập nhật ?', 'Cật nhật thành công', formRankUpdate);
                },
                error: function (e) {
                    if (e.responseText === "1") {
                        dangerAlert('Tên hạng đã tồn tại');
                    } else {
                        errorAlert('Lỗi.');
                    }
                    console.clear();
                }
            });
        }
    }

    function deleteRank() {
        const id = document.getElementById('idRank').value;
        const url = "http://localhost:8080/mangostore/admin/rank/delete/" + id;
        confirmAlertLink(event, "Bạn muốn xóa ?", "Xóa thành công", url);
    }
}

const addRank = document.querySelector('.addRankPage');
if (addRank) {
    function addRankForm() {
        const nameRank = document.getElementById('nameRankInput').value;
        const minimumScore = document.getElementById('minimumScore').value;
        const maximumScore = document.getElementById('maximumScore').value;
        if (nameRank === '') {
            dangerAlert('Tên hạng không được để trống');
        } else if (minimumScore <= 0) {
            dangerAlert('Điểm tối thiểu không thể nhỏ hơn hoặc bằng 0');
        } else if (maximumScore <= 0) {
            dangerAlert('Điểm tối đa không thể nhỏ hơn hoặc bằng 0');
        } else if (minimumScore >= maximumScore) {
            dangerAlert('Điểm tối đa phải lớn hơn điểm tối thiểu');
        } else {
            const data = {
                nameRank: nameRank,
            }
            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080' + '/api/mangostore/admin/rank/check-add',
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: 'application/json',
                success: function (response) {
                    const formVoucher = document.getElementById('formRank');
                    confirmAlertForm('Ban muốn thêm ?', 'Thêm thành công', formVoucher);
                },
                error: function (e) {
                    if (e.responseText === "1") {
                        dangerAlert('Tên hạng đã tồn tại');
                    } else {
                        errorAlert('Lỗi.');
                    }
                    console.clear();
                }
            });
        }
    }
}

const restoreRankPage = document.querySelector(".restoreRankPage");
if (restoreRankPage) {
    function restoreRank(button) {
        const id = button.getAttribute('data-id');
        const url = "http://localhost:8080/mangostore/admin/rank/restore/" + id;
        confirmAlertLink(event, 'Bạn muốn khôi phục ?', 'Khôi phục thành công', url);
    }
}