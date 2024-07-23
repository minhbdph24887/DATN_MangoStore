const sortDatePage = document.querySelector('.sortDatePage');
if (sortDatePage) {
    document.addEventListener('DOMContentLoaded', function () {
        const yearSelect = document.getElementById('year');
        const currentYear = new Date().getFullYear();
        const urlParams = new URLSearchParams(window.location.search);
        const selectedYear = urlParams.get('fillerByYears');
        const selectedQuarter = urlParams.get('fitterByPrecious');

        for (let year = currentYear; year >= 1900; year--) {
            const option = document.createElement('option');
            option.value = year;
            option.text = year;
            if (year == selectedYear) {
                option.selected = true;
            }
            yearSelect.appendChild(option);
        }

        if (selectedQuarter) {
            const quarterSelect = document.getElementById('fitterByPrecious');
            quarterSelect.value = selectedQuarter;
        }

        // Initialize Select2
        $('#year').select2();

        // Set selected value for Select2 if there is a selectedYear
        if (selectedYear) {
            $('#year').val(selectedYear).trigger('change');
        }
    });

    function updateUrl() {
        const yearSelect = document.getElementById('year').value;
        const quarterSelect = document.getElementById('fitterByPrecious').value;
        const currentUrl = new URL(window.location.href);

        const currentYearParam = currentUrl.searchParams.get('fillerByYears');
        const currentQuarterParam = currentUrl.searchParams.get('fitterByPrecious');

        let urlUpdated = false;

        if (yearSelect && yearSelect !== currentYearParam) {
            currentUrl.searchParams.set('fillerByYears', yearSelect);
            urlUpdated = true;
        }

        if (quarterSelect !== 'default' && quarterSelect !== currentQuarterParam) {
            currentUrl.searchParams.set('fitterByPrecious', quarterSelect);
            urlUpdated = true;
        }

        if (urlUpdated) {
            window.location.href = currentUrl.toString();
        }
    }
}