const comboboxSelect2Page = document.querySelector(".comboboxSelect2");
if (comboboxSelect2Page) {
    $(document).ready(function () {
        $('.js-example-basic-multiple').select2();
        function formatProduct (product) {
            if (!product.id) { return product.text; }
            var $product = $(
                '<span"><img src="' + $(product.element).data('image') + '" class="img-thumbnail" style="width: 30px; height: 30px;"/> ' + product.text + '</span>'
            );
            return $product;
        };
        $('#idProduct').select2({
            templateResult: formatProduct,
            templateSelection: function(product) {
                return product.text;
            }
        }).on('change', function() {
            var imageUrl = $(this).find(':selected').data('image');
            $('#product-image').attr('src', imageUrl).show();
        });
    });
}