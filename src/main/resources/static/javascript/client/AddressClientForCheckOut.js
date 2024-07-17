const addAddressClientPage = document.querySelector(".addAddressClient");
if (addAddressClientPage) {
    function AddAddressClient() {
        const nameClient = document.getElementById('nameClientInput').value;
        const numberClient = document.getElementById('numberPhoneInput').value;
        const city = document.querySelector('.city-select').value;
        const district = document.querySelector('.district-select').value;
        const ward = document.querySelector('.ward-select').value;
        const specificAddress = document.getElementById('specificAddress').value;

        const phoneRegex = /^[0-9]{10}$/;
        if (nameClient === '') {
            dangerAlert('Please Enter The Name Client');
        } else if (numberClient === '') {
            dangerAlert('Please Enter The Number Client');
        } else if (!phoneRegex.test(numberClient)) {
            dangerAlert('Phone Format Is Incorrect');
        } else if (city === '') {
            dangerAlert('Please Choose The Province');
        } else if (district === '') {
            dangerAlert('Please Choose The District');
        } else if (ward === '') {
            dangerAlert('Please Choose The Commune');
        } else if (specificAddress === '') {
            dangerAlert('Please Enter The Specific Address');
        } else {
            const addressClientForm = document.getElementById('addressClientForm');
            confirmAlertForm('Do You Want Add Address Client?', 'Add Address Client Successfully', addressClientForm);
        }
    }

    $(document).ready(function () {
        $('.edit-btn').click(function (event) {
            event.preventDefault();
            const id = $(this).data('id');
            const nameClient = $(this).data('nameclient');
            const phoneNumber = $(this).data('phonenumber');
            const province = $(this).data('province');
            const district = $(this).data('district');
            const commune = $(this).data('commune');
            const specificAddress = $(this).data('specificaddress');
            openEditModal(id, nameClient, phoneNumber, province, district, commune, specificAddress);
            $('#myModal1').modal('hide')
        });

        async function openEditModal(id, nameClient, phoneNumber, province, district, commune, specificAddress) {
            document.getElementById('editAddressId').value = id;
            document.getElementById('editNameClientInput').value = nameClient;
            document.getElementById('editNumberPhoneInput').value = phoneNumber;
            document.getElementById('editSpecificAddress').value = specificAddress;
            await loadDistrictsAndWards(province, district, commune);
            $('#editAddressClientModal').modal('show');
        }

        async function loadDistrictsAndWards(province, district, commune) {
            $('.edit-city-select').val(province).trigger('change');
            await new Promise(r => setTimeout(r, 500));
            $('.edit-district-select').val(district).trigger('change');
            await new Promise(r => setTimeout(r, 500));
            $('.edit-ward-select').val(commune);
        }
    });

    function updateAddressClient() {
        const editNameClient = document.getElementById('editNameClientInput').value;
        const editNumberPhone = document.getElementById('editNumberPhoneInput').value;
        const city = document.querySelector('.edit-city-select').value;
        const district = document.querySelector('.edit-district-select').value;
        const ward = document.querySelector('.edit-ward-select').value;
        const editSpecificAddress = document.getElementById('editSpecificAddress').value;

        const phoneRegex = /^[0-9]{10}$/;
        if (editNameClient === '') {
            dangerAlert('Please Enter The Name Client');
        } else if (editNumberPhone === '') {
            dangerAlert('Please Enter The Number Client');
        } else if (!phoneRegex.test(editNumberPhone)) {
            dangerAlert('Phone Format Is Incorrect');
        } else if (city === '') {
            dangerAlert('Please Choose The Province');
        } else if (district === '') {
            dangerAlert('Please Choose The District');
        } else if (ward === '') {
            dangerAlert('Please Choose The Commune');
        } else if (editSpecificAddress === '') {
            dangerAlert('Please Enter The Specific Address');
        } else {
            const editAddressForm = document.getElementById('editAddressForm');
            confirmAlertForm('Do You Want Update Address Client?', 'Update Address Client Successfully', editAddressForm);
        }
    }
}