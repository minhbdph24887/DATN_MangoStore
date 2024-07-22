const addRolesPage = document.querySelector('.AddRole');
const formRoles = document.getElementById("formRole");
if (addRolesPage) {
    function addRole() {
        const CodeRoleInput = document.getElementById('CodeRoleInput').value;
        const nameInput = document.getElementById('NameInput').value;

        const uppercaseRegex = /^[A-Z]+$/;
        if (CodeRoleInput === '') {
            dangerAlert('Please Enter Code Role');
            return false;
        } else if (uppercaseRegex.test(CodeRoleInput) === false) {
            dangerAlert('Please nhập chữ in hoa');
            return false;
        } else if (nameInput === '') {
            dangerAlert('Please Enter Name Role');
            return false;
        } else {
            // formRoles.submit();
            const data = {
                codeRole: CodeRoleInput,
            }
            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080' + '/api/mangostore/admin/role/check-role',
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: 'application/json',
                success: function () {
                    const formRole = document.getElementById("formRole");
                    confirmAlertForm('Bạn có muốn thêm không', 'Thêm thành công', formRole);
                },
                error: function (e) {
                    if (e.responseText === "1") {
                        dangerAlert('Code Role is already in used');
                    }
                }
            });
        }
    }
}
const restoreRolePage = document.querySelector('.restoreRolePage');
if (restoreRolePage) {
    function restoreRole(button) {
        const idRole = button.getAttribute('data-id');
        const url = 'http://localhost:8080/mangostore/admin/role/restore/' + idRole;
        confirmAlertLink(event, 'Bạn có muốn khôi phục không', 'Khôi phục thành công', url);

    }
}
// const deleteRolePage = document.querySelector('.deleteRolePage');
// if (deleteRolePage) {
//     function deleteRole() {
//         const idRole = document.getElementById("idRole").value;
//         const url = 'http://localhost:8080/mangostore/admin/role/delete/' + idRole
//         confirmAlertLink(event, 'Bạn có muốn xóa không', 'Xóa thành công', url);
//
//     }
// }