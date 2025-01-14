
document.addEventListener('DOMContentLoaded', function () {
    const searchTypeDropdown = document.getElementById('search-type');
    const searchInput = document.getElementById('search-input');
    const searchButton = document.getElementById('search-btn');
    const tableBody = document.getElementById('branch-table');
    const pagination = document.getElementById('pagination');
    const resultCount = document.getElementById('result-count');

    let branchData = [];        // 모든 지점 데이터
    let filteredData = [];      // 검색된 지점 데이터
    let currentPage = 1;        // 현재 페이지
    const itemsPerPage = 5;     // 테이블에 표시할 항목 수

    // 모든 마커 및 선택된 마커 관리
    let markers = [];
    let selectedMarker = null;
    const markersMap = new Map();

    // Kakao 지도 생성
    const mapContainer = document.getElementById('map');
    const mapOption = {
        center: new kakao.maps.LatLng(37.5665, 126.9780), // 서울 중심 좌표
        level: 5
    };
    const map = new kakao.maps.Map(mapContainer, mapOption);

    // 인포윈도우 생성
    const infowindow = new kakao.maps.InfoWindow({
        zIndex: 1,
        removable: true // 닫기 버튼 활성화
    });

    // 기본 마커 이미지
    const defaultMarkerImage = new kakao.maps.MarkerImage(
        'https://t1.daumcdn.net/mapjsapi/images/marker.png',
        new kakao.maps.Size(24, 35)
    );

    // 선택된 마커 이미지
    const highlightedMarkerImage = new kakao.maps.MarkerImage(
        'http://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png',
        new kakao.maps.Size(24, 35)
    );

    // 마커를 기본 이미지로 되돌리는 함수
    function resetMarkerStyle(marker) {
        if (marker) {
            marker.setZIndex(1);
            marker.setImage(defaultMarkerImage);
        }
    }

    // 선택된 마커를 강조(별 모양)하는 함수
    function highlightMarker(marker) {
        if (selectedMarker) resetMarkerStyle(selectedMarker);
        marker.setZIndex(2);
        marker.setImage(highlightedMarkerImage);
        selectedMarker = marker;
    }

    // 인포 패널에 지점 정보 업데이트
    function updateInfoPanel(branch) {
        if (!branch) return;
        document.getElementById('input-branch-id').value = branch.branchId || '-';
        document.getElementById('input-branch-name').value = branch.branchName || '-';
        document.getElementById('input-branch-postcode').value = branch.postAddress || '-';
        document.getElementById('input-branch-address').value = branch.branchAddress || '-';
        document.getElementById('input-branch-date').value = branch.lastModifiedAt || '-';
    }

    // 검색 결과 건수 업데이트
    function updateResultCount(count) {
        resultCount.textContent = count;
    }

    // 모든 마커를 지도에 표시
    function displayAllMarkers(data) {
        markers.forEach(marker => marker.setMap(null));
        markers = [];
        markersMap.clear();

        data.forEach(branch => {
            const marker = new kakao.maps.Marker({
                position: new kakao.maps.LatLng(branch.branchLatitude, branch.branchLongitude),
                map: map,
                title: branch.branchName,
                image: defaultMarkerImage
            });

            markers.push(marker);
            markersMap.set(branch.branchId, { marker, branch });

            kakao.maps.event.addListener(marker, 'click', function () {
                highlightMarker(marker);
                updateInfoPanel(branch);
                map.setCenter(marker.getPosition()); // 마커를 지도 중심으로 이동
                infowindow.setContent(`<div style="padding:5px; font-size:12px">${branch.branchName}</div>`);
                infowindow.open(map, marker);
            });
        });
    }

    // 테이블에 데이터 업데이트
    function updateTable(data) {
        tableBody.innerHTML = '';

        if (data.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="3">No Data</td></tr>';
            updateResultCount(0);
            pagination.innerHTML = '';
            return;
        }

        updateResultCount(data.length);

        // **총 페이지 수 계산**
        const totalPages = Math.ceil(data.length / itemsPerPage);
        if (currentPage > totalPages) currentPage = totalPages;
        if (currentPage < 1) currentPage = 1;

        const startIndex = (currentPage - 1) * itemsPerPage;
        const endIndex = startIndex + itemsPerPage;
        const pageData = data.slice(startIndex, endIndex);

        pageData.forEach(branch => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${branch.branchName}</td>
                <td>${branch.branchAddress}</td>
                <td>
    <button style="background-color: #FFD028; color: #0D183F; font-weight: bold; border: none; padding: 8px 12px; border-radius: 4px; cursor: pointer;"
            onclick="selectBranch('${branch.branchId}')">
        선택
    </button>
</td>
            `;
            tableBody.appendChild(row);
        });

        updatePaginationControls(totalPages, data);
    }

    // 페이지네이션 컨트롤 업데이트
    function updatePaginationControls(totalPages, data) {
        pagination.innerHTML = '';

        if (totalPages <= 1) return;

        const createPageButton = (text, onClick, disabled = false) => {
            const btn = document.createElement('button');
            btn.textContent = text;
            btn.disabled = disabled;
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

        let startPage = Math.max(1, currentPage - 2);
        let endPage = Math.min(totalPages, startPage + 4);
        if (endPage - startPage < 4) {
            startPage = Math.max(1, endPage - 4);
        }

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

    window.selectBranch = function (branchId) {
        const markerInfo = markersMap.get(branchId);
        if (markerInfo) {
            const { marker, branch } = markerInfo;
            highlightMarker(marker);
            updateInfoPanel(branch);
            map.setCenter(marker.getPosition());
            infowindow.setContent(`<div style="padding:5px; font-size: 12px;">${branch.branchName}</div>`);
            infowindow.open(map, marker);
        }
    };

    searchButton.addEventListener('click', function () {
        const searchType = searchTypeDropdown.value;
        const searchKeyword = searchInput.value.trim();

        if (!searchType || !searchKeyword) {
            alert('검색 조건과 키워드를 입력해주세요.');
            return;
        }

        filteredData = branchData.filter(branch => {
            return searchType === 'branchName'
                ? branch.branchName.includes(searchKeyword)
                : branch.branchAddress.includes(searchKeyword);
        });

        currentPage = 1;
        displayAllMarkers(filteredData);
        updateTable(filteredData);
    });

    axios.get("/map/branches")
        .then((response) => {
            branchData = response.data
            displayAllMarkers(branchData);
            updateTable(branchData);

        })
        .catch((error) => {
            console.error(error)
        });
});

