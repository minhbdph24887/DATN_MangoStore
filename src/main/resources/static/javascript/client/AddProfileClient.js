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
            dangerAlert('Please Enter The Name Client');
        } else if (!phoneRegex.test(numberPhone) && numberPhone !== '') {
            dangerAlert('Phone Format Is Incorrect');
        } else if (email === '') {
            dangerAlert('Please Enter The Email');
        } else if (!emailRegex.test(email) && email !== '') {
            dangerAlert('Email Format Is Incorrect');
        } else if (!selectedGenderMethod) {
            dangerAlert('Please Choose Gender');
        } else if (!validateBirthday(birthday)) {
            dangerAlert('You must be at least 8 years old');
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
                    confirmAlertForm('Do You Want to Update Profile', 'Update Profile Successfully', fromUpdateProfile);
                },
                error: function (e) {
                    if (e.responseText === "1") {
                        dangerAlert('Phone number is already in used');
                    } else if (e.responseText === "2") {
                        dangerAlert('Email has been used');
                    } else {
                        errorAlert('An error occurred.');
                    }
                    console.log(e);
                }
            });
        }
    }
}