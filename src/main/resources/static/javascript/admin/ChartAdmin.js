const chartPage = document.querySelector('.chartPage');
if (chartPage) {
    document.addEventListener("DOMContentLoaded", function () {
        const urlParams = new URLSearchParams(window.location.search);
        const year = urlParams.get('fillerByYears') || new Date().getFullYear();

        fetch(`/mangostore/admin/monthly-revenue?year=${year}`)
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
    });
}