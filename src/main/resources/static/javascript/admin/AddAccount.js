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
            dangerAlert('Please Enter Full Name');
            return false;
        } else if (numberPhone === '') {
            dangerAlert('Please Enter Number Phone');
            return false;
        } else if (!phoneRegex.test(numberPhone)) {
            dangerAlert('Phone Format Is Incorrect');
            return false;
        } else if (email === '') {
            dangerAlert("Please Enter email");
            return false;
        } else if (!emailRegex.test(email)) {
            dangerAlert("Email Format Incorrect");
            return false;
        } else if (date === '') {
            dangerAlert("Please Enter birthday");
            return false;
        } else if (date !== '' && age < 18) {
            dangerAlert("Age must be greater than 18 ");
            return false;
        } else if (pass === "") {
            dangerAlert("Please Enter password");
            return false;
        } else if (!passRegex.test(pass)) {
            dangerAlert("Password Format Incorrect");
            return false;
        } else if (!EnterpassRegex.test(Enterpass)) {
            dangerAlert("Please Enter password");
            return false;
        } else if (pass !== Enterpass) {
            dangerAlert("Enterpass  the same Pass");
            return false;
        } else {
            const data = {
                numberPhone: numberPhone,
                email: email
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
                        dangerAlert('Phone number is already in used');
                    } else if (e.responseText === "2") {
                        dangerAlert('Email has been used');
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
        const phoneRegex = /^[0-9]{10}$/;
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const passRegex = /^[a-zA-Z0-9]{8,16}$/;
        const EnterpassRegex = /^[a-zA-Z0-9]{8,16}$/;
        if (fullName === '') {
            dangerAlert('Please Enter Full Name');
            return false;
        } else if (numberPhone === '') {
            dangerAlert('Please Enter Number Phone');
            return false;
        } else if (!phoneRegex.test(numberPhone)) {
            dangerAlert('Phone Format Is Incorrect');
            return false;
        } else if (email === '') {
            dangerAlert("Please Enter email");
            return false;
        } else if (!emailRegex.test(email)) {
            dangerAlert("Email Format Incorrect");
            return false;
        } else if (!passRegex.test(pass) && pass !== '') {
            dangerAlert("Password Format Incorrect");
            return false;
        } else if (!EnterpassRegex.test(Enterpass) && Enterpass !== '') {
            dangerAlert("Please Enter password");
            return false;
        } else if (pass !== Enterpass) {
            dangerAlert("Enterpass the same Pass");
            return false;
        } else {
            const data = {
                idUpdate: idUpdate,
                numberPhone: numberPhone,
                email: email
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
                    if (e.responseText === "1") {
                        dangerAlert('Phone number is already in used');
                    } else if (e.responseText === "2") {
                        dangerAlert('Email has been used');
                    }
                }
            });
        }

    }

}
const deleteAccountPage = document.querySelector('.deleteAccountPage');
if (deleteAccountPage) {
    function deleteAccount() {
        const idAccount = document.getElementById("idStaff").value;
        const url = 'http://localhost:8080/mangostore/admin/list-staff/delete/' + idAccount
        confirmAlertLink(event, 'Bạn có muốn xóa không', 'Xóa thành công', url);

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
if(deleteClientPage){
    function deleteClient(){
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