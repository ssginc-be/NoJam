let currentPage = 1;
const itemsPerPage = 10;

// 발주 내역 가져오기 및 검색 조건 적용
function fetchOrderHistory(page = 1) {
    console.log("searchCategory: " + document.getElementById('searchCategory'))
    const searchCategory = document.getElementById('searchCategory').value || 'orderId'; // 선택된 검색 카테고리
    // const searchCategory = document.getElementById('searchCategory').value || 'default_category';
    const searchInput = document.getElementById('searchInput').value.trim() || '';

    console.log(searchCategory + " " + searchInput);

    fetch('/order/history/orderhistory') // 서버에서 발주 데이터 요청
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            let filteredData = data.items;

            // 검색 조건 적용
            if (searchInput) {
                filteredData = filteredData.filter(item => {
                    const value = item[searchCategory]?.toString().toLowerCase();
                    return value.includes(searchInput.toLowerCase());
                });
            }

            // 검색 결과 개수 업데이트
            document.getElementById('searchResult').innerHTML = `총 <span class="fw-bold">${filteredData.length}건</span>이 검색되었습니다`;

            // 현재 페이지와 테이블 업데이트
            currentPage = page;
            updateOrderTable(filteredData);
        })
        .catch(error => console.error('Error loading order history:', error));
}

// 테이블 업데이트
function updateOrderTable(data) {
    const tableBody = document.querySelector('#itemTable tbody');
    tableBody.innerHTML = ''; // 기존 테이블 데이터 초기화

    const totalPages = Math.ceil(data.length / itemsPerPage);
    if (currentPage > totalPages) currentPage = totalPages;
    if (currentPage < 1) currentPage = 1;

    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const pageData = data.slice(startIndex, endIndex);

    pageData.forEach(item => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td><input type="checkbox" data-orderid="${item.orderId}"></td>
            <td>${item.orderId}</td>
            <td>${item.itemId}</td>
            <td>${item.name}</td>
            <td>${item.category1}</td>
            <td>${item.category2}</td>
            <td>${item.quantity}</td>
            <td>${item.orderTime}</td>
            <td>${item.status}</td>
        `;
        tableBody.appendChild(row);
    });

    // 페이지네이션 업데이트
    updatePaginationControls(totalPages, data);
}

// 페이지네이션 컨트롤 업데이트
function updatePaginationControls(totalPages, data) {
    const pagination = document.getElementById('pagination');
    pagination.innerHTML = ''; // 기존 버튼 초기화

    if (totalPages <= 1) return;

    const createPageButton = (text, onClick, disabled = false) => {
        const button = document.createElement('button');
        button.textContent = text;
        button.disabled = disabled;
        button.className = `btn btn-sm ${disabled ? 'btn-secondary' : 'btn-light'}`;
        button.addEventListener('click', onClick);
        pagination.appendChild(button);
    };

    createPageButton('<<', () => fetchOrderHistory(1), currentPage === 1);
    createPageButton('<', () => fetchOrderHistory(currentPage - 1), currentPage === 1);

    const startPage = Math.max(1, currentPage - 2);
    const endPage = Math.min(totalPages, startPage + 4);

    for (let i = startPage; i <= endPage; i++) {
        createPageButton(i, () => fetchOrderHistory(i), i === currentPage);
    }

    createPageButton('>', () => fetchOrderHistory(currentPage + 1), currentPage === totalPages);
    createPageButton('>>', () => fetchOrderHistory(totalPages), currentPage === totalPages);
}

// // 발주 취소: 선택된 발주를 취소하는 함수
// function cancelSelectedOrders() {
//     const checkboxes = document.querySelectorAll('tbody input[type="checkbox"]:checked');
//     if (checkboxes.length === 0) {
//         alert('취소할 발주를 선택하세요.');
//         return;
//     }
//
//     const orderIds = Array.from(checkboxes).map(cb => cb.getAttribute('data-orderid'));
//
//     // 서버로 POST 요청을 보내 선택된 발주를 취소
//     fetch('/order/history/cancel', {
//         method: 'POST',
//         headers: {'Content-Type': 'application/json'},
//         body: JSON.stringify(orderIds),
//     })
//         .then(response => response.text())
//         .then(result => {
//             alert(result); // 결과 메시지 출력
//             fetchOrderHistory(currentPage); // 현재 페이지 데이터 새로고침
//         })
//         .catch(error => {
//             console.error('Error canceling orders:', error);
//             alert('취소 중 오류가 발생했습니다.');
//         });
// }

document.getElementById('searchButton').addEventListener('click', () => {
    fetchOrderHistory(); // 첫 페이지부터 검색
});

// 초기화 버튼: 검색 조건 초기화
function resetSearch() {
    document.getElementById('searchCategory').value = 'orderId';
    document.getElementById('searchInput').value = '';
    fetchOrderHistory(); // 첫 페이지 데이터 로드
}

// DOM 로드 후 이벤트 리스너 등록
window.onload = () => {
    document.getElementById('searchButton').addEventListener('click', () => fetchOrderHistory());
    document.getElementById('resetButton').addEventListener('click', resetSearch);
    // document.getElementById('cancelButton').addEventListener('click', cancelSelectedOrders);
    fetchOrderHistory(); // 초기 데이터 로드
};