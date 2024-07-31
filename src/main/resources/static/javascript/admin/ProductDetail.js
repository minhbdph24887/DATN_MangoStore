refreshData();

function applyFilters() {
    var formData = {
        keyword: getInputValue('input[name="keyword"]'),
        materialId: getSelectValue('select[name="material"]'),
        sizeId: getSelectValue('select[name="size"]'),
        colorId: getSelectValue('select[name="color"]'),
        originId: getSelectValue('select[name="origin"]'),
        categoryId: getSelectValue('select[name="category"]'),
        sortBy: getSelectValue('select[name="sortByPrice"]'),
        page: 0,  // Not needed if we are not paginating
        size: 1000  // Fetch a large number of products to ensure we get all
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

            // Log the received data for debugging purposes
            console.log("Data received:", data);
        },
        error: function(xhr, status, error) {
            console.error('Error:', error);
        }
    });
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
