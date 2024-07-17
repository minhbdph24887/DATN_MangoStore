const showPassWord = document.querySelector(".showPassWordPage");
if (showPassWord) {
    document.addEventListener('DOMContentLoaded', (event) => {
        const togglePasswordButton = document.querySelector('.btn.btn-primary');
        const passwordInput = document.getElementById('passwordInput');
        const confirmPasswordInput = document.getElementById('confirmPasswordInput');

        function togglePassword() {
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);
            confirmPasswordInput.setAttribute('type', type);
            togglePasswordButton.textContent = type === 'password' ? 'Show Password' : 'Hide Password';
        }

        togglePasswordButton.addEventListener('click', togglePassword);
    });
}

const checkPassWordClient = document.querySelector(".checkPasswordClientPage");
if (checkPassWordClient) {
    document.addEventListener("DOMContentLoaded", function () {
        const passwordInput = document.getElementById("passwordInput");
        const confirmPasswordInput = document.getElementById("confirmPasswordInput");
        const passwordError = document.getElementById("passwordError");
        const confirmPasswordError = document.getElementById("confirmPasswordError");
        const saveButton = document.getElementById("saveButton");

        saveButton.disabled = true;

        function validatePassword() {
            const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^\s]{8,16}$/;
            const password = passwordInput.value;
            const confirmPassword = confirmPasswordInput.value;

            if (!passwordRegex.test(password)) {
                passwordError.style.display = "block";
                saveButton.disabled = true;
            } else {
                passwordError.style.display = "none";
                saveButton.disabled = confirmPassword !== password;
            }

            if (confirmPassword !== password) {
                confirmPasswordError.style.display = "block";
                saveButton.disabled = true;
            } else {
                confirmPasswordError.style.display = "none";
            }
        }

        passwordInput.addEventListener("input", validatePassword);
        confirmPasswordInput.addEventListener("input", validatePassword);
    });
}

const updateChangePassword = document.querySelector(".updateChangePasswordPage");
if (updateChangePassword) {
    function UpdateChangePassword() {
        const formChangePassword = document.getElementById('formChangePassword');
        confirmAlertForm("Do you want to update change password ?", "Update Successful", formChangePassword);
    }
}