const currentDate = document.querySelector('.current-date');
const daysTag = document.querySelector('.days');
const prevNextIcon = document.querySelectorAll('.material-icons');
let selectedDate = ''; // 사용자가 선택한 날짜를 저장

let date = new Date();
let currYear = date.getFullYear();
let currMonth = date.getMonth();

const months = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'November',
    'December',
];

const renderCalendar = () => {
    let lastDateofMonth = new Date(currYear, currMonth + 1, 0).getDate();
    let firstDayofMonth = new Date(currYear, currMonth, 1).getDay();
    let lastDayofMonth = new Date(currYear, currMonth, lastDateofMonth).getDay();
    let lastDateofLastMonth = new Date(currYear, currMonth, 0).getDate();

    let liTag = '';

    for (let i = firstDayofMonth; i > 0; i--) {
        liTag += `<li class="inactive">${lastDateofLastMonth - i + 1}</li>`;
    }

    for (let i = 1; i <= lastDateofMonth; i++) {
        let isSelected =
            selectedDate === `${currYear}-${String(currMonth + 1).padStart(2, '0')}-${String(i).padStart(2, '0')}`
                ? 'selected'
                : '';
        liTag += `<li class="${isSelected}" onclick="selectDate(${i})">${i}</li>`;
    }

    for (let i = lastDayofMonth; i < 6; i++) {
        liTag += `<li class="inactive">${i - lastDayofMonth + 1}</li>`;
    }

    currentDate.innerHTML = `${months[currMonth]} ${currYear}`;
    daysTag.innerHTML = liTag;
};

const selectDate = (day) => {
    // 선택한 날짜를 저장
    selectedDate = `${currYear}-${String(currMonth + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
    renderCalendar(); // 캘린더 다시 렌더링하여 선택 날짜 강조
};

prevNextIcon.forEach((icon) => {
    icon.addEventListener('click', () => {
        currMonth = icon.id === 'prev' ? currMonth - 1 : currMonth + 1;

        if (currMonth < 0 || currMonth > 11) {
            date = new Date(currYear, currMonth);
            currYear = date.getFullYear();
            currMonth = date.getMonth();
        } else {
            date = new Date();
        }

        renderCalendar();
    });
});

$('#datePickerModal').on('show.bs.modal', () => {
    date = new Date();
    currYear = date.getFullYear();
    currMonth = date.getMonth();
    renderCalendar();
});

const applyDate = () => {
    console.log(selectedDate)
    $('#datePickerModal').modal('hide')
};

renderCalendar();
