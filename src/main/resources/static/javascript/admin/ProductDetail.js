const searchProduct = document.querySelector(".searchProductPage");
if (searchProduct) {
    refreshData();

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
                // Removed pagination logic
            },
            error: function(xhr, status, error) {
                console.error('Error:', error);
            }
        });
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
}