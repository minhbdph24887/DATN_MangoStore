const chartPage = document.querySelector('.chartPage');
if (chartPage) {
    document.addEventListener("DOMContentLoaded", function () {
        function updateChart() {
            const urlParams = new URLSearchParams(window.location.search);
            const year = urlParams.get('fillerByYears');
            const quarter = urlParams.get('fitterByPrecious');
            const startDate = urlParams.get('startDate');
            const endDate = urlParams.get('endDate');

            if (year && !startDate && !endDate) {
                fetch(`/mangostore/admin/monthly-revenue?year=${year}&quarter=${quarter}`)
                    .then(response => response.json())
                    .then(data => {
                        const labels = data.map(item => `Tháng ${item.month}`);
                        const revenues = data.map(item => item.totalRevenue);

                        const ctx = document.getElementById('monthly-revenue-chart').getContext('2d');
                        new Chart(ctx, {
                            type: 'bar',
                            data: {
                                labels: labels,
                                datasets: [{
                                    label: 'Doanh thu',
                                    data: revenues,
                                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                                    borderColor: 'rgba(75, 192, 192, 1)',
                                    borderWidth: 1
                                }]
                            },
                            options: {
                                scales: {
                                    y: {
                                        beginAtZero: true
                                    }
                                }
                            }
                        });
                    });
            } else if (startDate && endDate && !year && !quarter) {
                fetch(`/mangostore/admin/monthly-revenue?startDate=${startDate}&endDate=${endDate}`)
                    .then(response => response.json())
                    .then(data => {
                        const labels = data.map(item => `Ngày ${new Date(item.date).toLocaleDateString()}`);
                        const revenues = data.map(item => item.totalRevenue);

                        const ctx = document.getElementById('monthly-revenue-chart').getContext('2d');
                        new Chart(ctx, {
                            type: 'bar',
                            data: {
                                labels: labels,
                                datasets: [{
                                    label: 'Doanh thu',
                                    data: revenues,
                                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                                    borderColor: 'rgba(75, 192, 192, 1)',
                                    borderWidth: 1
                                }]
                            },
                            options: {
                                scales: {
                                    y: {
                                        beginAtZero: true
                                    }
                                }
                            }
                        });
                    });
            } else {
                const today = new Date().toISOString().slice(0, 10);
                fetch(`/mangostore/admin/monthly-revenue?startDate=${today}&endDate=${today}`)
                    .then(response => response.json())
                    .then(data => {
                        const labels = data.map(item => `Ngày ${new Date(item.date).toLocaleDateString()}`);
                        const revenues = data.map(item => item.totalRevenue);

                        const ctx = document.getElementById('monthly-revenue-chart').getContext('2d');
                        new Chart(ctx, {
                            type: 'bar',
                            data: {
                                labels: labels,
                                datasets: [{
                                    label: 'Doanh thu',
                                    data: revenues,
                                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                                    borderColor: 'rgba(75, 192, 192, 1)',
                                    borderWidth: 1
                                }]
                            },
                            options: {
                                scales: {
                                    y: {
                                        beginAtZero: true
                                    }
                                }
                            }
                        });
                    });
            }
        }

        updateChart();

        document.querySelector('#year').addEventListener('change', function () {
            updateChart();
        });
        document.querySelector('#fitterByPrecious').addEventListener('change', function () {
            updateChart();
        });
    });
}

const chartStaff = document.querySelector(".chartStaffPage");
if (chartStaff) {
    document.addEventListener("DOMContentLoaded", function () {
        function updateChart() {
            const urlParams = new URLSearchParams(window.location.search);
            const startDate = urlParams.get('startDate') || new Date().toISOString().slice(0, 10);
            const endDate = urlParams.get('endDate') || new Date().toISOString().slice(0, 10);

            fetch(`/mangostore/admin/monthly-revenue?startDate=${startDate}&endDate=${endDate}`)
                .then(response => response.json())
                .then(data => {
                    const labels = data.map(item => `Ngày ${new Date(item.date).toLocaleDateString()}`);
                    const revenues = data.map(item => item.totalRevenue);

                    const ctx = document.getElementById('monthly-revenue-chart').getContext('2d');
                    new Chart(ctx, {
                        type: 'bar',
                        data: {
                            labels: labels,
                            datasets: [{
                                label: 'Doanh thu',
                                data: revenues,
                                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                                borderColor: 'rgba(75, 192, 192, 1)',
                                borderWidth: 1
                            }]
                        },
                        options: {
                            scales: {
                                y: {
                                    beginAtZero: true
                                }
                            }
                        }
                    });
                });
        }

        updateChart();

        document.querySelector('#startDate').addEventListener('change', function () {
            updateChart();
        });
        document.querySelector('#endDate').addEventListener('change', function () {
            updateChart();
        });
    });
}