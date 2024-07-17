// vinh

refreshData();

var currentPage = 0; // Biến lưu trữ trang hiện tại, mặc định là trang đầu tiên
var totalPages = 0; // Giá trị này sẽ được cập nhật từ server

function applyFilters() {
    var formData = {
        keyword: getInputValue('input[name="keyword"]'),
        materialId: getSelectValue('select[name="material"]'),
        sizeId: getSelectValue('select[name="size"]'),
        colorId: getSelectValue('select[name="color"]'),
        originId: getSelectValue('select[name="origin"]'),
        categoryId: getSelectValue('select[name="category"]'),
        sortBy: getSelectValue('select[name="sortByPrice"]'),
        page: 0,  // Trang đầu tiên khi áp dụng bộ lọc
        size: 10  // Số lượng sản phẩm trên mỗi trang
    };

    fetchData(formData);
}

function getInputValue(selector) {
    var element = document.querySelector(selector);
    return element ? element.value.trim() : '';
}

function getSelectValue(selector) {
    var element = document.querySelector(selector);
    return element ? element.value : '';
}

function fetchData(formData) {
    console.log(formData);

    $.ajax({
        url: '/mangostore/admin/product-detail',
        type: 'GET',
        data: formData,
        success: function(data) {
            $('#content2').find('tbody').html($(data).find('tbody').html());

            // Lấy thông tin từ response để cập nhật phân trang
            var totalPagesElement = $(data).find('#totalPages');
            var currentPageElement = $(data).find('#currentPageDisplay span');

            console.log("Total Pages Element:", totalPagesElement);
            console.log("Current Page Element:", currentPageElement);

            if (totalPagesElement.length > 0 && currentPageElement.length > 0) {
                totalPages = parseInt(totalPagesElement.text());
                currentPage = parseInt(currentPageElement.text());
            } else {
                console.error("Could not find totalPages or currentPage in the response.");
            }

            // Ghi log để kiểm tra giá trị của totalPages và currentPage
            console.log("Total Pages:", totalPages);
            console.log("Current Page:", currentPage);

            var totalItems = parseInt($(data).find('#totalItems').text());

            updatePagination(totalPages, currentPage, totalItems);
        },
        error: function(xhr, status, error) {
            console.error('Error:', error);
        }
    });
}


function updatePagination(totalPages, currentPage, totalItems) {
    // Cập nhật các nút phân trang
    $('#firstPage').toggleClass('disabled', currentPage === 0);
    $('#previousPage').toggleClass('disabled', currentPage === 0);
    $('#nextPage').toggleClass('disabled', currentPage >= totalPages - 1);
    $('#lastPage').toggleClass('disabled', currentPage >= totalPages - 1);

    // Cập nhật thông tin trang hiện tại
    document.getElementById('currentPageDisplay').innerHTML = 'Current Page: ' + (currentPage + 1);
}

function refreshData() {
    // Clear filter fields
    document.querySelector('input[name="keyword"]').value = "";
    document.querySelector('select[name="material"]').selectedIndex = 0;
    document.querySelector('select[name="size"]').selectedIndex = 0;
    document.querySelector('select[name="color"]').selectedIndex = 0;
    document.querySelector('select[name="origin"]').selectedIndex = 0;
    document.querySelector('select[name="category"]').selectedIndex = 0;

    // Optionally, you can reset the sortByPrice select as well
    document.querySelector('select[name="sortByPrice"]').selectedIndex = 0;

    // Trigger applyFilters to fetch data with default parameters
    applyFilters();
}

function nextPage() {
    console.log("Current Page before next:", currentPage);

    if (currentPage < totalPages - 1) {
        currentPage += 1;
        goToPage(currentPage);
    }
}

function previousPage() {
    console.log("Current Page before previous:", currentPage);

    if (currentPage > 0) {
        currentPage -= 1;
        goToPage(currentPage);
    }
}

function goToPage(pageNumber) {
    // Cập nhật formData với pageNumber mới
    var formData = {
        keyword: getInputValue('input[name="keyword"]'),
        materialId: getSelectValue('select[name="material"]'),
        sizeId: getSelectValue('select[name="size"]'),
        colorId: getSelectValue('select[name="color"]'),
        originId: getSelectValue('select[name="origin"]'),
        categoryId: getSelectValue('select[name="category"]'),
        sortBy: "asc",  // Sắp xếp theo giá tăng dần
        page: pageNumber,  // Trang mới
        size: 10  // Số lượng sản phẩm trên mỗi trang (có thể lấy từ biến size nếu cần)
    };

    document.getElementById('currentPageDisplay').innerHTML = 'Current Page: ' + (pageNumber + 1);

    console.log("Page number going to:", pageNumber);
    // Gọi fetchData với formData mới
    fetchData(formData);
}
