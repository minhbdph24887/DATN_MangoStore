const checkProductClientPage = document.querySelector('.productClientPage');
if (checkProductClientPage) {
    function sortProducts() {
        const selectedOption = document.getElementById('priceSortSelect').value;
        const keyword = document.querySelector('input[name="keyword"]').value;
        const currentUrl = window.location.href;
        let newUrl = currentUrl.split('?')[0];
        if (keyword) {
            newUrl += '?keyword=' + encodeURIComponent(keyword);
        }
        if (selectedOption) {
            newUrl += (keyword ? '&' : '?') + 'sortDirection=' + encodeURIComponent(selectedOption);
        }
        window.location.href = newUrl;
    }

    function changePage(pageNo) {
        const selectedOption = document.getElementById('priceSortSelect').value;
        const keyword = document.querySelector('input[name="keyword"]').value;
        let newUrl = window.location.href.split('?')[0];
        const params = [];

        if (keyword) {
            params.push('keyword=' + encodeURIComponent(keyword));
        }
        if (selectedOption) {
            params.push('sortDirection=' + encodeURIComponent(selectedOption));
        }
        params.push('pageNo=' + pageNo);

        newUrl += '?' + params.join('&');
        window.location.href = newUrl;
    }
}
