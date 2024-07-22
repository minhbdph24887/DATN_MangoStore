const addAddressPage = document.querySelector('.AddAddress');
const form123= document.getElementById("formAddAddress123");
if (addAddressPage) {
    function addAddress() {
        const nameClinet = document.getElementById('nameClientInput').value;
        const phoneNumber = document.getElementById('phoneNumberInput').value;
        console.log("name"+nameClinet);
        const phoneAddressRegex = /^[0-9]{10}$/;
        if (nameClinet === '') {
            dangerAlert('Please Enter Full Name Client');
            return false;
        } else if (phoneNumber === '') {
            dangerAlert('Please Enter Phone Number');
            return false;

        } else if (!phoneAddressRegex.test(phoneNumber)) {
            dangerAlert('Phone Number Format Is Incorrect');
            return false;
        }else {
            form123.submit();
            return true;
        }
    }
}