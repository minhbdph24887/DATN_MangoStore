const SendEmailPage = document.querySelector('.SendEmailPage');
if (SendEmailPage) {
    function sendEmail() {
        const emailInput = document.getElementById('emailInput').value;

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (emailInput === '') {
            dangerAlert('Vui lòng nhập email');
        } else if (!emailRegex.test(emailInput)) {
            dangerAlert('Đinh dạng email không hợp lệ');
        } else {
            const data = {
                email: emailInput,
            }
            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080' + '/api/mangostore/login/check-send-email',
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: 'application/json',
                success: function () {
                    const fromSendEmail = document.getElementById("fromSendEmail");
                    fromSendEmail.submit();
                },
                error: function (e) {
                    dangerAlert(e.responseText);
                    console.clear();
                }
            });
        }
    }
}

const ForgotPasswordPage = document.querySelector(".ForgotPasswordPage");
if (ForgotPasswordPage) {
    function forgotPassword() {
        const codeForgotInput = document.getElementById("codeForgotInput").value;
        const data = {
            codeForgot: codeForgotInput,
        }
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080' + '/api/mangostore/login/check-forgot-password',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json',
            success: function () {
                const formForgotPassword = document.getElementById("formForgotPassword");
                formForgotPassword.submit();
            },
            error: function (e) {
                dangerAlert(e.responseText);
                console.clear();
            }
        });
    }
}