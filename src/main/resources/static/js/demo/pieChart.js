let pieCt = document.getElementById('pieChart');

async function getOutgoingStatus() {
    try {
        const response = await axios.get("/chart/statusCount");

        const label = response.data.map(item => item.status);
        const data = response.data.map(item => item.percentage);

        pieChart.data.labels = label;
        pieChart.data.datasets[0].data = data;

        pieChart.update();
    } catch (error) {
        console.log(error);
    }
}

let pieChart = new Chart(pieCt, {
    type: 'doughnut',
    data: {
        labels: [],
        datasets: [
            {
                data: [],
                backgroundColor: ['#4e73df', '#1cc88a'],
                hoverBackgroundColor: ['#2e59d9', '#17a673'],
                hoverBorderColor: "rgba(234, 236, 244, 1)",
            }
        ],
    },
    options: {
        maintainAspectRatio: false,
        tooltips: {
            backgroundColor: "rgb(255,255,255)",
            bodyFontColor: "#858796",
            borderColor: '#dddfeb',
            borderWidth: 1,
            xPadding: 15,
            yPadding: 15,
            displayColors: false,
            caretPadding: 10,
        },
        plugins: {
            legend: {
                display: false
            },
        },
        cutoutPercentage: 80,
    },
})

getOutgoingStatus();