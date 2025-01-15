let areaCt = document.getElementById('areaChart');

async function getCategoryLabels() {
    try {
        // 카테고리 목록 가져오기
        const categories = await axios.get("/chart/categoryLabel");

        let initSales = "";
        let initIncoming = "";

        if (userRole === "HEAD") {  // 본사인 경우 branchId가 없음
            initSales = await axios.get("/chart/salesByCategory?branchId=");
            initIncoming = await axios.get("/chart/incomingByCategory?branchId=");
        } else {                    // 지점인 경우 branchId를 넘겨주어야 함.
            initSales = await axios.get("/chart/salesByCategory?branchId=" + branchId);
            initIncoming = await axios.get("/chart/incomingByCategory?branchId=" + branchId);
        }

        // 가져온 카테고리 목록으로 x축의 레이블 설정
        areaChart.data.labels = categories.data;

        // 가져온 카테고리별 입고량으로 데이터 설정
        areaChart.data.datasets[1].data = initSales.data;
        // 가져온 카테고리별 판매량으로 데이터 설정
        areaChart.data.datasets[0].data = initIncoming.data;

        // 설정된 값들로 chart update
        areaChart.update();
    } catch (error) {
        console.log(error);
    }
}

function changeSelect2() {
    // select 태그 가져오기
    let branchSelect = document.getElementById("selectedBranch2");
    // 선택된 option의 value 가져오기
    let selectValue = String(branchSelect.options[branchSelect.selectedIndex].value);

    // 선택된 항목으로 chart update
    axios.get("/chart/salesByCategory?branchId=" + selectValue)
        .then((response) => {
            areaChart.data.datasets[1].data = response.data;

            return axios.get("/chart/incomingByCategory?branchId=" + selectValue);
        })
        .then((response) => {
            areaChart.data.datasets[0].data = response.data;

            areaChart.update();
        })
        .catch((error) => {
            console.log(error);
        })
}

let areaChart = new Chart(areaCt, {
    type: 'line',
    data: {
        labels: [],
        datasets: [
            {
                label: '입고량',
                data: [],
            },
            {
                label: '판매량',
                data: [],
            }
        ]
    },
    options: {
        maintainAspectRatio: false,
        responsive: true,
        plugins: {
            legend: {
                display: false
            },
            tooltip: {
                enabled: true,
                mode: 'index',
                intersect: false,
                callbacks: {
                    label: function (context) {
                        const label = context.dataset.label || '';
                        const value = context.raw;
                        const index = context.dataIndex;

                        // 퍼센트를 "sales" 데이터셋에서만 계산
                        if (context.dataset.label === '판매량') {
                            const incomingValue = context.chart.data.datasets[0].data[index] || 0; // Incoming 값
                            const percent = incomingValue > 0
                                ? ((value / incomingValue) * 100).toFixed(2)
                                : 'N/A';
                            return [
                                `${label}: ${value}`,
                                `(입고 대비: ${percent}%)`
                            ];
                        }

                        // incoming 데이터셋은 값만 표시
                        return `${label}: ${value}`;
                    }
                }
            },
        }
    }
});

getCategoryLabels();