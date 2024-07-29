const SetPasswordPage = document.querySelector(".SetPasswordPage");
if (SetPasswordPage) {
    function setPassword() {
        const passwordInput = document.getElementById("passwordInput").value;
        const passwordConfirm = document.getElementById("passwordConfirm").value;

        const passRegex = /^[a-zA-Z0-9]{8,16}$/;
        if (passwordInput === "") {
            dangerAlert('Vui lòng nhập mật khẩu');
        } else if (!passRegex.test(passwordInput)) {
            dangerAlert('Mật khẩu không đúng định dạng');
        } else if (passwordConfirm === '') {
            dangerAlert('Vui lòng nhập lại mật khẩu');
        } else if (!passRegex.test(passwordConfirm)) {
            dangerAlert('Nhập lại mật khẩu không đúng định dạng');
        } else if (passwordInput !== passwordConfirm) {
            dangerAlert('Mật khẩu không khớp nhau');
        } else {
            const formSetPassword = document.getElementById("formSetPassword");
            formSetPassword.submit();
        }
    }
}