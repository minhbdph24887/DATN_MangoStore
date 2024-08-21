const searchProduct = document.querySelector(".searchProductPage");
if(searchProduct) {
    refreshData();
    let currentPage = 0;
    let totalPages = 0;

    function applyFilters() {
        const formData = {
            keyword: getInputValue('input[name="keyword"]'),
            materialId: getSelectValue('select[name="material"]'),
            sizeId: getSelectValue('select[name="size"]'),
            colorId: getSelectValue('select[name="color"]'),
            originId: getSelectValue('select[name="origin"]'),
            categoryId: getSelectValue('select[name="category"]'),
            sortBy: getSelectValue('select[name="sortByPrice"]'),
        };

        fetchData(formData);
    }

    function getInputValue(selector) {
        const element = document.querySelector(selector);
        return element ? element.value.trim() : '';
    }

    function getSelectValue(selector) {
        const element = document.querySelector(selector);
        return element ? element.value : '';
    }

    function fetchData(formData) {
        $.ajax({
            url: '/mangostore/admin/product-detail',
            type: 'GET',
            data: formData,
            success: function(data) {
                $('#content2').find('tbody').html($(data).find('tbody').html());
                const totalPagesElement = $(data).find('#totalPages');
                const currentPageElement = $(data).find('#currentPageDisplay span');

                if (totalPagesElement.length > 0 && currentPageElement.length > 0) {
                    totalPages = parseInt(totalPagesElement.text());
                    currentPage = parseInt(currentPageElement.text());
                }

                const totalItems = parseInt($(data).find('#totalItems').text());

                updatePagination(totalPages, currentPage, totalItems);
            },
            error: function(xhr, status, error) {
                console.error('Error:', error);
            }
        });
    }


    function updatePagination(totalPages, currentPage, totalItems) {
        $('#firstPage').toggleClass('disabled', currentPage === 0);
        $('#previousPage').toggleClass('disabled', currentPage === 0);
        $('#nextPage').toggleClass('disabled', currentPage >= totalPages - 1);
        $('#lastPage').toggleClass('disabled', currentPage >= totalPages - 1);
    }

    function refreshData() {
        document.querySelector('input[name="keyword"]').value = "";
        document.querySelector('select[name="material"]').selectedIndex = 0;
        document.querySelector('select[name="size"]').selectedIndex = 0;
        document.querySelector('select[name="color"]').selectedIndex = 0;
        document.querySelector('select[name="origin"]').selectedIndex = 0;
        document.querySelector('select[name="category"]').selectedIndex = 0;
        document.querySelector('select[name="sortByPrice"]').selectedIndex = 0;
        applyFilters();
    }

    function nextPage() {
        if (currentPage < totalPages - 1) {
            currentPage += 1;
            goToPage(currentPage);
        }
    }

    function previousPage() {
        if (currentPage > 0) {
            currentPage -= 1;
            goToPage(currentPage);
        }
    }

    function goToPage(pageNumber) {
        const formData = {
            keyword: getInputValue('input[name="keyword"]'),
            materialId: getSelectValue('select[name="material"]'),
            sizeId: getSelectValue('select[name="size"]'),
            colorId: getSelectValue('select[name="color"]'),
            originId: getSelectValue('select[name="origin"]'),
            categoryId: getSelectValue('select[name="category"]'),
            sortBy: "asc",
        };

        document.getElementById('currentPageDisplay').innerHTML = 'Current Page: ' + (pageNumber + 1);
        fetchData(formData);
    }
}