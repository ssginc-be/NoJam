// 현재 페이지와 페이지당 항목 수
let currentPage = 1;
const itemsPerPage = 10;

// 상품 선택 시 동작
// 테이블 행을 클릭했을 때 선택된 상품 정보를 입력 필드에 반영
function selectItem(row) {
    const cells = row.getElementsByTagName('td');
    const itemId = cells[0].innerText;
    const itemName = cells[1].innerText;
    const price = cells[4].innerText;

    document.getElementById('selectedItem').value = `${itemId} - ${itemName}`;
    document.getElementById('selectedItem').dataset.itemId = itemId;
    document.getElementById('selectedItem').dataset.price = price;
}

// 상품 데이터 로드 및 검색
// 검색 조건에 따라 상품 데이터를 필터링하고 테이블에 반영
function fetchItems(page = 1) {
    const searchCategory = document.getElementById('searchCategory').value; // 선택된 검색 카테고리
    const searchInput = document.getElementById('searchInput').value.trim(); // 검색어

    fetch('/order/items') // 서버에서 상품 데이터 요청
        .then(response => response.json())
        .then(data => {
            // 검색 조건 적용
            if (searchInput) {
                data = data.filter(item => {
                    const value = item[searchCategory]?.toString().toLowerCase();
                    return value.includes(searchInput.toLowerCase());
                });
            }

            // 검색 결과 개수 업데이트
            document.getElementById('searchResult').innerHTML = `총 <span class="fw-bold">${data.length}건</span>이 검색되었습니다`;

            // 현재 페이지와 테이블 업데이트
            currentPage = page;
            updateTable(data);
        })
        .catch(error => console.error('Error loading items:', error));
}

// 테이블 업데이트
// 현재 페이지 데이터만 테이블에 표시
function updateTable(data) {
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
        row.onclick = () => selectItem(row);
        row.innerHTML = `
            <td>${item.itemId}</td>
            <td>${item.name}</td>
            <td>${item.category1}</td>
            <td>${item.category2}</td>
            <td>${item.price}</td>
        `;
        tableBody.appendChild(row);
    });

    updatePaginationControls(totalPages, data);
}

// 페이지네이션 컨트롤 업데이트
// 페이지네이션 버튼 생성 및 이벤트 연결
function updatePaginationControls(totalPages, data) {
    const pagination = document.querySelector('.pagination');
    pagination.innerHTML = ''; // 기존 버튼 초기화

    if (totalPages <= 1) return;

    const createPageButton = (text, onClick, disabled = false) => {
        const btn = document.createElement('button');
        btn.textContent = text;
        btn.disabled = disabled;
        btn.className = `btn btn-sm ${disabled ? 'btn-secondary' : 'btn-light'}`;
        btn.addEventListener('click', onClick);
        pagination.appendChild(btn);
    };

    createPageButton('<<', () => {
        currentPage = 1;
        updateTable(data);
    }, currentPage === 1);

    createPageButton('<', () => {
        if (currentPage > 1) currentPage--;
        updateTable(data);
    }, currentPage === 1);

    const startPage = Math.max(1, currentPage - 2);
    const endPage = Math.min(totalPages, startPage + 4);

    for (let i = startPage; i <= endPage; i++) {
        createPageButton(i, () => {
            currentPage = i;
            updateTable(data);
        }, i === currentPage);
    }

    createPageButton('>', () => {
        if (currentPage < totalPages) currentPage++;
        updateTable(data);
    }, currentPage === totalPages);

    createPageButton('>>', () => {
        currentPage = totalPages;
        updateTable(data);
    }, currentPage === totalPages);
}

// 발주 목록에 추가
// 선택된 상품과 수량을 발주 목록에 추가
document.getElementById('addOrder').addEventListener('click', () => {
    const itemInput = document.getElementById('selectedItem');
    const quantityInput = document.getElementById('quantity');
    const itemId = itemInput.dataset.itemId;
    const itemName = itemInput.value.split(' - ')[1];
    const price = itemInput.dataset.price;
    const quantity = quantityInput.value;

    if (!itemId || !quantity) {
        alert('상품을 선택하고 수량을 입력하세요.');
        return;
    }

    const totalPrice = price * quantity;

    const tableBody = document.querySelector('#orderTable tbody');
    const row = document.createElement('tr');
    row.innerHTML = `
        <td><input type="checkbox"></td>
        <td>${itemId}</td>
        <td>${itemName}</td>
        <td>${quantity}</td>
        <td>${totalPrice}</td>
    `;
    tableBody.appendChild(row);

    quantityInput.value = '';
    itemInput.value = '';
    document.getElementById('totalPrice').value = 0;
});

// 발주 신청 버튼
// 선택된 발주 목록을 서버에 전송
document.getElementById('submitOrders').addEventListener('click', () => {
    const orders = [];
    document.querySelectorAll('#orderTable tbody input[type="checkbox"]:checked').forEach(checkbox => {
        const row = checkbox.closest('tr');
        const cells = row.getElementsByTagName('td');
        orders.push({
            itemId: cells[1].innerText,
            quantity: parseInt(cells[3].innerText, 10),
        });
    });

    if (orders.length === 0) {
        alert('발주할 항목을 선택하세요.');
        return;
    }

    fetch('/order/register', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(orders),
    })
        .then(response => {
            if (response.ok) {
                alert('발주가 성공적으로 등록되었습니다.');
                location.reload();
            } else {
                alert('발주 등록에 실패했습니다.');
            }
        })
        .catch(error => console.error('Error submitting orders:', error));
});

// 선택 항목 삭제
// 선택된 발주 목록 삭제
document.getElementById('deleteSelected').addEventListener('click', () => {
    document.querySelectorAll('#orderTable tbody input[type="checkbox"]:checked').forEach(checkbox => {
        checkbox.closest('tr').remove();
    });
    document.getElementById('selectAll').checked = false; // 전체 선택 체크박스 해제
});

// 전체 선택 체크박스
// 모든 항목 체크/체크 해제
document.getElementById('selectAll').addEventListener('change', (event) => {
    const isChecked = event.target.checked;
    document.querySelectorAll('#orderTable tbody input[type="checkbox"]').forEach(checkbox => {
        checkbox.checked = isChecked;
    });
});

// 검색 버튼
// 검색 기능 구현
document.getElementById('searchButton').addEventListener('click', () => {
    fetchItems(1); // 첫 페이지에서 검색 수행
});

// 초기화 버튼
// 검색어 초기화 및 첫 페이지 데이터 로드
document.getElementById('resetButton').addEventListener('click', () => {
    document.getElementById('searchInput').value = '';
    fetchItems(1);
});

// 초기 데이터 로드 및 수량 변경 이벤트 처리
// 페이지 로드 시 초기 데이터와 수량 계산 설정
window.onload = () => {
    fetchItems();
    document.getElementById('quantity').addEventListener('input', () => {
        const price = document.getElementById('selectedItem').dataset.price;
        const quantity = document.getElementById('quantity').value;
        document.getElementById('totalPrice').value = price * quantity || 0;
    });
};