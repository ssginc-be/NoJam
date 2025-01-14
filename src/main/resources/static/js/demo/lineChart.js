let lineCt = document.getElementById('lineChart')

async function initLineChart() {
    try {
        // 처음 페이지 접속 시 endDate를 오늘 날짜로 설정
        let initDate = new Date();
        let initYear = initDate.getFullYear();
        let initMonth = initDate.getMonth() + 1;
        let initDay = initDate.getDate();

        let endDate = `${initYear}-${String(initMonth).padStart(2, '0')}-${String(initDay).padStart(2, '0')}`;

        let response = ""

        if (userRole === "HEAD") {
            response = await axios.get("/chart/salesByDayOfWeek?endDate=" + endDate + "&branchId=");
        } else {
            response = await axios.get("/chart/salesByDayOfWeek?endDate=" + endDate + "&branchId=" + branchId);
        }

        const initDayOfWeekArray = response.data.map(item => item.dayOfWeek);
        const initSumOfSalesArray = response.data.map(item => item.sumOfSales);

        lineChart.data.labels = initDayOfWeekArray;
        lineChart.data.datasets[0].data = initSumOfSalesArray;

        lineChart.update();
    } catch (error) {
        console.log(error);
    }

}

function applyDateAndBranch() {
    if (selectedDate === "") {
        // 처음 페이지 접속 시 endDate를 오늘 날짜로 설정
        let initDate = new Date();
        let initYear = initDate.getFullYear();
        let initMonth = initDate.getMonth() + 1;
        let initDay = initDate.getDate();

        selectedDate = `${initYear}-${String(initMonth).padStart(2, '0')}-${String(initDay).padStart(2, '0')}`;
    }

    // select 태그 가져오기
    let branchSelect = document.getElementById("selectedBranch3");
    // 선택된 option의 value 가져오기
    let selectValue = String(branchSelect.options[branchSelect.selectedIndex].value);

    axios.get("/chart/salesByDayOfWeek?endDate=" + selectedDate + "&branchId=" + selectValue)
        .then((response) => {
            const initDayOfWeekArray = response.data.map(item => item.dayOfWeek);
            const initSumOfSalesArray = response.data.map(item => item.sumOfSales);

            lineChart.data.labels = initDayOfWeekArray;
            lineChart.data.datasets[0].data = initSumOfSalesArray;

            lineChart.update();
        })
        .catch((error) => {
            console.log(error);
        })
}

let lineChart = new Chart(lineCt, {
    type: 'line',
    data: {
        labels: [],
        datasets: [
            {
                label: '판매량',
                data: []
            }
        ]
    },
    options: {
        maintainAspectRatio: false,
        plugins: {
            legend: {
                display: false
            }
        }
    }
});

initLineChart();