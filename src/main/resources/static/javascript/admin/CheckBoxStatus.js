const checkBoxStatusElement = document.querySelector('.checkBoxStatus');
if (checkBoxStatusElement) {
    function initializeStatus() {
        const isActive = document.getElementById('active').checked ? 1 : 0;
        document.getElementById('status').value = isActive;
    }

    document.getElementById('active').addEventListener('change', initializeStatus);

    initializeStatus();
}