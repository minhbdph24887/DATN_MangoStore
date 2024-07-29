const addAccountPage = document.querySelector('.AddAccount');
if (addAccountPage) {
    function addAccount() {
        const fullName = document.getElementById('fullNameInput').value;
        const numberPhone = document.getElementById('numberPhoneInput').value;
        const email = document.getElementById('emailInput').value;
        const date = document.getElementById("birthdaytInput").value;
        const pass = document.getElementById("encryptionPassword").value;
        const Enterpass = document.getElementById("EnterPassword").value;
        const phoneRegex = /^[0-9]{10}$/;
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const passRegex = /^[a-zA-Z0-9]{8,16}$/;
        const EnterpassRegex = /^[a-zA-Z0-9]{8,16}$/;
        const dateParts = date.split('-');
        const year = dateParts[0];
        const currentYear = new Date().getFullYear();
        const age = currentYear - year;
        if (fullName === '') {
            dangerAlert('Họ tên không được để trống');
        } else if (numberPhone === '') {
            dangerAlert('Số điện thoại không được để trống');
        } else if (!phoneRegex.test(numberPhone)) {
            dangerAlert('Số điện thoại không đúng định dạng');
        } else if (email === '') {
            dangerAlert("Email không được để trống");
        } else if (!emailRegex.test(email)) {
            dangerAlert("Email không đúng định dạng");
        } else if (date === '') {
            dangerAlert("Ngày sinh không được để trống");
        } else if (date !== '' && age < 18) {
            dangerAlert("Bạn phải trên 18 tuổi");
        } else if (pass === "") {
            dangerAlert("Mật khẩu không được để trống");
        } else if (!passRegex.test(pass)) {
            dangerAlert("Mật khẩu không đúng đinh dạng");
        } else if (Enterpass === '') {
            dangerAlert("Mật khẩu nhập lại không được để trống")
        } else if (!EnterpassRegex.test(Enterpass)) {
            dangerAlert("Nhập lại mật khẩu không đúng định dạng");
        } else if (pass !== Enterpass) {
            dangerAlert("Mật khẩu với nhâp lại mật khẩu không trùng khớp");
        } else {
            const data = {
                numberPhone: numberPhone,
                email: email,
            }
            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080' + '/api/mangostore/admin/account/check-add',
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: 'application/json',
                success: function () {
                    const form = document.getElementById("formAddAccount");
                    confirmAlertForm('Bạn có muốn thêm không', 'Thêm thành công', form);
                },
                error: function (e) {
                    if (e.responseText === "1") {
                        dangerAlert('Số điện thoại đã tồn tại');
                    } else if (e.responseText === "2") {
                        dangerAlert('Email đã tồn tại');
                    }
                }
            });
        }
    }
}
const updateAccountPage = document.querySelector('.updateAccountPage');
if (updateAccountPage) {
    function updateAccount() {
        const idUpdate = document.getElementById('idStaff').value;
        const fullName = document.getElementById('fullNameInput').value;
        const numberPhone = document.getElementById('numberPhoneInput').value;
        const email = document.getElementById('emailInput').value;
        const pass = document.getElementById("encryptionPassword").value;
        const Enterpass = document.getElementById("EnterPassword").value;
        const statusAccount = document.getElementById('statusAccount').value;

        const phoneRegex = /^[0-9]{10}$/;
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const passRegex = /^[a-zA-Z0-9]{8,16}$/;
        const EnterpassRegex = /^[a-zA-Z0-9]{8,16}$/;
        if (fullName === '') {
            dangerAlert('Họ tên không được để trống');
        } else if (numberPhone === '') {
            dangerAlert('Số điện thoại không được để trống');
        } else if (!phoneRegex.test(numberPhone)) {
            dangerAlert('Số điện thoại không đúng định dạng');
        } else if (email === '') {
            dangerAlert("Email không được để trống");
        } else if (!emailRegex.test(email)) {
            dangerAlert("Email không đúng định dạng");
        } else if (!passRegex.test(pass) && pass !== '') {
            dangerAlert("Mật khẩu không đúng đinh dạng");
        } else if (!EnterpassRegex.test(Enterpass) && Enterpass !== '') {
            dangerAlert("Nhập lại mật khẩu không đúng đinh dạng");
        } else if (pass !== Enterpass) {
            dangerAlert("Mật khẩu với nhâp lại mật khẩu không trùng khớp");
        } else {

            const data = {
                idUpdate: idUpdate,
                numberPhone: numberPhone,
                email: email,
                statusAccount: statusAccount,
            }
            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080' + '/api/mangostore/admin/account/check-update',
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: 'application/json',
                success: function () {
                    const formUpdate = document.getElementById("formUpdate");
                    confirmAlertForm('Bạn có muốn sửa không', 'Sửa thành công', formUpdate);
                },
                error: function (e) {
                    console.clear();
                    if (e.responseText === "1") {
                        dangerAlert('Số điện thoại đã được sử dụng');
                    } else if (e.responseText === "2") {
                        dangerAlert('Email đã được sử dụng');
                    } else if (e.responseText === "3") {
                        dangerAlert('Hiện tại còn đúng 1 tài khoản có quyền ADMIN, bạn không thể trạng thái tài khoản');
                    }
                }
            });
        }

    }

}
const deleteAccountPage = document.querySelector('.deleteAccountPage');
if (deleteAccountPage) {
    function deleteAccount(event) {
        event.preventDefault();
        const idAccount = document.getElementById("idStaff").value;
        const data = {
            idAddAccount: idAccount,
        }
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080' + '/api/mangostore/admin/account/check-delete',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json',
            success: function () {
                const url = 'http://localhost:8080/mangostore/admin/list-staff/delete/' + idAccount
                confirmAlertLink(event, 'Bạn có muốn xóa không', 'Xóa thành công', url);
            },
            error: function (e) {
                console.clear();
                if (e.responseText === '1') {
                    dangerAlert('Hiện tại còn đúng 1 tài khoản có quyền ADMIN, bạn không thể xóa');
                }
            }
        });
    }
}

const restorePage = document.querySelector('.restorePage');
if (restorePage) {
    function restoreStaff(button) {
        const idAccount = button.getAttribute('data-id');
        const url = 'http://localhost:8080/mangostore/admin/list-staff/restore/' + idAccount;
        confirmAlertLink(event, 'Bạn có muốn khôi phục không', 'Khôi phục thành công', url);

    }

}
const deleteClientPage = document.querySelector('.deleteClientPage');
if (deleteClientPage) {
    function deleteClient() {
        const idClient = document.getElementById("idStaff").value;
        const url = 'http://localhost:8080/mangostore/admin/list-client/delete/' + idClient
        confirmAlertLink(event, 'Bạn có muốn xóa không', 'Xóa thành công', url);
    }
}
const restoreClientPage = document.querySelector('.restoreClientPage');
if (restoreClientPage) {
    function restoreClient(button) {
        const idAccount = button.getAttribute('data-id');
        const url = 'http://localhost:8080/mangostore/admin/list-client/restore/' + idAccount;
        confirmAlertLink(event, 'Bạn có muốn khôi phục không', 'Khôi phục thành công', url);

    }
}