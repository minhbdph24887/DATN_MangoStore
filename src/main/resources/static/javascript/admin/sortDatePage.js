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

        $('#year').select2();

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

const fitterByDate = document.querySelector(".fitterByDatePage");
if (fitterByDate) {
    document.addEventListener("DOMContentLoaded", function () {
        const startDateInput = document.getElementById("startDate");
        const endDateInput = document.getElementById("endDate");

        function getQueryParam(param) {
            const urlParams = new URLSearchParams(window.location.search);
            return urlParams.get(param);
        }

        function showAlert(message) {
            dangerAlert(message);
        }

        function clearInput(input) {
            input.value = "";
        }

        function validateDates() {
            const startDateValue = startDateInput.value;
            const endDateValue = endDateInput.value;

            const startDate = new Date(startDateValue);
            const endDate = new Date(endDateValue);
            const today = new Date();

            if (startDateValue) {
                if (startDate > today) {
                    showAlert("Ngày bắt đầu không thể là ngày trong tương lai");
                    clearInput(startDateInput);
                    return false;
                }
            }

            if (endDateValue) {
                if (endDate > today) {
                    showAlert("Ngày kết thúc không thể là ngày trong tương lai");
                    clearInput(endDateInput);
                    return false;
                }

                if (startDateValue && endDate < startDate) {
                    showAlert("Ngày kết thúc không thể trước ngày bắt đầu");
                    clearInput(endDateInput);
                    return false;
                }

                if (!startDateValue) {
                    showAlert("Vui lòng nhập ngày bắt đầu trước");
                    clearInput(endDateInput);
                    return false;
                }
            }

            return true;
        }

        const startDateFromUrl = getQueryParam("startDate");
        const endDateFromUrl = getQueryParam("endDate");

        if (startDateFromUrl) {
            startDateInput.value = startDateFromUrl;
        }

        if (endDateFromUrl) {
            endDateInput.value = endDateFromUrl;
        }

        startDateInput.addEventListener("change", function () {
            if (startDateInput.value) {
                validateDates();
            }
        });

        endDateInput.addEventListener("change", function () {
            if (validateDates()) {
                const startDateValue = startDateInput.value;
                const endDateValue = endDateInput.value;

                if (startDateValue && endDateValue) {
                    window.location.href = `http://localhost:8080/mangostore/admin/home?startDate=${startDateValue}&endDate=${endDateValue}`;
                }
            }
        });
    });
}