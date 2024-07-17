const sortDatePage = document.querySelector('.sortDatePage');
if (sortDatePage) {
    $(document).ready(function () {
        $('#year').select2();

        const urlParams = new URLSearchParams(window.location.search);
        const selectedYear = urlParams.get('fillerByYears');

        if (selectedYear) {
            const yearSelect = document.getElementById('year');
            if (yearSelect.value !== selectedYear) {
                $('#year').val(selectedYear).trigger('change');
            }
        }
    });

    function sortByYear() {
        const yearSelect = document.getElementById('year').value;
        const currentUrl = new URL(window.location.href);
        const currentYearParam = currentUrl.searchParams.get('fillerByYears');

        if (currentYearParam !== yearSelect) {
            currentUrl.searchParams.set('fillerByYears', yearSelect);
            window.location.href = currentUrl.toString();
        }
    }
}