const RegisterPage = document.querySelector('.RegisterPage');
if (RegisterPage) {
    function registerAccount() {
        const fullNameInput = document.getElementById('fullNameInput').value;
        const emailInput = document.getElementById('emailInput').value;
        const passwordInput = document.getElementById('passwordInput').value;
        const passwordConfirm = document.getElementById('passwordConfirm').value;

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const passRegex = /^[a-zA-Z0-9]{8,16}$/;
        if (fullNameInput === '') {
            dangerAlert('Vui lòng nhập tên tài khoản');
        } else if (emailInput === '') {
            dangerAlert('Vui lòng nhập email');
        } else if (!emailRegex.test(emailInput)) {
            dangerAlert('Vui lòng nhập đúng định dạng email');
        } else if (passwordInput === '') {
            dangerAlert('Vui lòng nhập mật khẩu');
        } else if (!passRegex.test(passwordInput)) {
            dangerAlert('Mật khẩu không đúng định dạng');
        } else if (passwordConfirm === '') {
            dangerAlert('Vui lòng nhập lại mật khẩu');
        } else if (!passRegex.test(passwordConfirm)) {
            dangerAlert('Nhập lại mật khẩu không đúng định dạng');
        } else if (passwordInput !== passwordConfirm) {
            dangerAlert('Mật khẩu không giống nhau');
        } else {
            const data = {
                email: emailInput,
            }
            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080' + '/api/mangostore/login/check-register',
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: 'application/json',
                success: function () {
                    const formRegister = document.getElementById("formRegister");
                    formRegister.submit();
                },
                error: function (e) {
                    dangerAlert(e.responseText);
                    console.clear();
                }
            });
        }
    }
}