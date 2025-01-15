let curDate = new Date();
let curYear = curDate.getFullYear();
let curMonth = curDate.getMonth() + 1;
let curDay = curDate.getDate()

function formatCurrency(value) {
    // 숫자를 변환하고 "₩" 추가
    return `₩${value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}`;
}

function getSalesAmountByDate() {
    // let today = `${curYear}-${String(curMonth).padStart(2, '0')}-${String(curDay).padStart(2, '0')}`;
    let today = '2025-01-10';

    let divTag = document.getElementById("daily");

    let branch = "";
    if (userRole !== "HEAD") {
        branch = branchId;
    }

    axios.get("/chart/salesAmountByDate?date=" + today + "&branchId=" + branch)
        .then((response) => {
            divTag.innerHTML = formatCurrency(response.data);
        })
        .catch((error) => {
            console.log(error);
        });
}

function getSalesAmountByMonth() {
    let today = `${curYear}-${String(curMonth).padStart(2, '0')}`;
    // let today = '2025-01';

    let divTag = document.getElementById("monthly");

    let branch = "";
    if (userRole !== "HEAD") {
        branch = branchId;
    }

    axios.get("/chart/salesAmountByMonth?date=" + today + "&branchId=" + branch)
        .then((response) => {
            divTag.innerHTML = formatCurrency(response.data);
        })
        .catch((error) => {
            console.log(error);
        });
}

function getSalesAmountByYear() {
    let today = `${curYear}`;
    // let today = '2025';

    let divTag = document.getElementById("yearly");

    let branch = "";
    if (userRole !== "HEAD") {
        branch = branchId;
    }

    axios.get("/chart/salesAmountByYear?date=" + today + "&branchId=" + branch)
        .then((response) => {
            divTag.innerHTML = formatCurrency(response.data);
        })
        .catch((error) => {
            console.log(error);
        });
}

getSalesAmountByDate();
getSalesAmountByMonth();
getSalesAmountByYear();