const checkBirthday = document.querySelector(".checkBirthday");
if (checkBirthday) {
    document.getElementById("birthdayInput").addEventListener("change", function () {
        const birthdayInput = document.getElementById("birthdayInput");
        const saveButton = document.querySelector("button.btn.btn-outline-success");
        const currentDate = new Date();
        const enteredDate = new Date(birthdayInput.value);
        if (enteredDate > currentDate) {
            birthdayInput.nextElementSibling.style.display = "inline";
            saveButton.disabled = true;
        } else {
            birthdayInput.nextElementSibling.style.display = "none";
            saveButton.disabled = false;
        }
    });
    document.getElementById("birthdayInput").dispatchEvent(new Event("change"));
}

const detailProfilePage = document.querySelector('.detailProfile');
if (detailProfilePage) {
    function validateBirthday(birthday) {
        const today = new Date();
        const birthDate = new Date(birthday);
        const ageDiff = today.getFullYear() - birthDate.getFullYear();
        const monthDiff = today.getMonth() - birthDate.getMonth();
        const dayDiff = today.getDate() - birthDate.getDate();

        if (ageDiff < 8) {
            return false;
        } else if (ageDiff === 8) {
            if (monthDiff < 0) {
                return false;
            } else if (monthDiff === 0 && dayDiff < 0) {
                return false;
            }
        }
        return true;
    }

    function UpdateProfileClient() {
        const idProfile = document.getElementById('idProfile').value;
        const nameClient = document.getElementById('nameClientInput').value;
        const numberPhone = document.getElementById('numberPhoneInput').value;
        const email = document.getElementById('emailInput').value;
        const selectedGenderMethod = document.querySelector('input[name="gender"]:checked');
        const birthday = document.getElementById('birthdayInput').value;

        const phoneRegex = /^[0-9]{10}$/;
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (nameClient === '') {
            dangerAlert('Tên khách hàng không được để trống');
        } else if (!phoneRegex.test(numberPhone) && numberPhone !== '') {
            dangerAlert('Số điện thoại không đúng định dạng');
        } else if (email === '') {
            dangerAlert('Vui lòng nhập email');
        } else if (!emailRegex.test(email) && email !== '') {
            dangerAlert('Email không đúng định dạng');
        } else if (!selectedGenderMethod) {
            dangerAlert('Vui long chọn giới tính');
        } else if (!validateBirthday(birthday)) {
            dangerAlert('Bạn phải trên 8 tuổi');
        } else {
            const data = {
                id: idProfile,
                numberPhone: numberPhone,
                email: email,
            }

            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080' + '/api/mangostore/profile/check-update',
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: 'application/json',
                success: function (response) {
                    const fromUpdateProfile = document.getElementById('fromUpdateProfile');
                    confirmAlertForm('Bạn có muốn cập nhật ?', 'Cập nhật thành công', fromUpdateProfile);
                },
                error: function (e) {
                    if (e.responseText === "1") {
                        dangerAlert('Số điện thoại đã được sử dụng');
                    } else if (e.responseText === "2") {
                        dangerAlert('Email đã được sử dụng');
                    } else {
                        errorAlert('Lỗi.');
                    }
                    console.clear();
                }
            });
        }
    }
}