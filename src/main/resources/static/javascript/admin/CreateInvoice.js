const indexSell = document.querySelector('.indexSell');
if (indexSell) {
    $(document).ready(function () {
        $("#createOrderButton").click(function () {
            $.ajax({
                type: "POST",
                url: "http://localhost:8080" + "/api/mangostore/admin/sell/create",
                success: function (response) {
                    window.open("http://localhost:8080/mangostore/admin/sell", "_self")
                },
                error: function (e) {
                    dangerAlert("Please pay your bills in advance");
                    console.clear();
                }
            });
        });
    });
}