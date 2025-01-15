let barCt = document.getElementById('barChart');

async function getCategoryLabels() {
    try {
        // 카테고리 목록 가져오기
        const categories = await axios.get("/chart/categoryLabel");
        let initSales = "";

        if (userRole === "HEAD") {  // 본사인 경우 branchId가 없음
            initSales = await axios.get("/chart/salesByCategory?branchId=");
        } else {                    // 지점인 경우 branchId를 넘겨주어야 함.
            initSales = await axios.get("/chart/salesByCategory?branchId=" + branchId);
        }

        // 가져온 카테고리 목록으로 x축의 레이블 설정
        barChart.data.labels = categories.data;
        // 가져온 카테고리별 판매량으로 데이터 설정
        barChart.data.datasets[0].data = initSales.data;

        // 설정된 값들로 chart update
        barChart.update();
    } catch (error) {
        console.log(error);
    }
}

function changeSelect() {
    // select 태그 가져오기
    let branchSelect = document.getElementById("selectedBranch");
    // 선택된 option의 value 가져오기
    let selectValue = String(branchSelect.options[branchSelect.selectedIndex].value);

    // 선택된 항목으로 chart update
    axios.get("/chart/salesByCategory?branchId=" + selectValue)
        .then((response) => {
            barChart.data.datasets[0].data = response.data;

            barChart.update();
        })
        .catch((error) => {
            console.log(error);
        })
}

let barChart = new Chart(barCt, {
    type: 'bar',
    data: {
        labels: [],
        datasets: [
            {
                label: 'Dataset',
                data: [],
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

getCategoryLabels();