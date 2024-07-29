const loginPage = document.querySelector('.loginPage');
if (loginPage) {
    function loginAccount() {
        const emailInput = document.getElementById('emailInput').value;
        const passwordInput = document.getElementById('passwordInput').value;

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const passRegex = /^[a-zA-Z0-9]{8,16}$/;
        if (emailInput === '') {
            dangerAlert('Vui lòng nhập email');
        } else if (!emailRegex.test(emailInput)) {
            dangerAlert('Vui lòng nhập đúng định dạng email');
        } else if (passwordInput === '') {
            dangerAlert('Vui lòng nhập mật khẩu');
        } else if (!passRegex.test(passwordInput)) {
            dangerAlert('Mật khẩu không đúng định dạng');
        } else {
            const data = {
                email: emailInput,
                password: passwordInput,
            }
            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080' + '/api/mangostore/login/check-login',
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: 'application/json',
                success: function () {
                    const formLogin = document.getElementById("fromLogin");
                    formLogin.submit();
                },
                error: function (e) {
                    dangerAlert(e.responseText);
                    console.clear();
                }
            });
        }
    }
}