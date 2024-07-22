const checkUpdateAuthenticationPage = document.querySelector('.checkUpdateAuthenticationPage');
if (checkUpdateAuthenticationPage) {
    function checkUpdateAuthentication() {
        const idAccount = document.getElementById('idAccount').value;
        const data = {
            idAccount: idAccount,
        }
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080' + '/api/mangostore/admin/authentication/check-update',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json',
            success: function () {
                const formUpdateAuthentication = document.getElementById('formUpdateAuthentication');
                confirmAlertForm('Bạn có muốn cập nhật không', 'Cập nhật thành công', formUpdateAuthentication);
            },
            error: function (e) {
                if (e.responseText === "1") {
                    dangerAlert('Tài khoản phải có ít nhất 1 quyền ADMIN');
                }
            }
        });
    }
}


