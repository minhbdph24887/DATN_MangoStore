
// vinh


const createProductDetailPage = document.querySelector(".createProductDetail");
if (createProductDetailPage) {
    function generateVariants() {
        const productValue = document.getElementById("idProduct");
        const productName = productValue.options[productValue.selectedIndex].dataset.name;

        const sizeSelect = document.getElementById("size");
        const selectedSizeOptions = sizeSelect.selectedOptions;

        const colorSelect = document.getElementById("color");
        const selectedColorOptions = colorSelect.selectedOptions;

        const resultTableBody = document.querySelector("#result tbody");
        resultTableBody.innerHTML = "";

        for (let i = 0; i < selectedSizeOptions.length; i++) {
            const sizeValue = selectedSizeOptions[i].value;

            for (let j = 0; j < selectedColorOptions.length; j++) {
                const colorValue = selectedColorOptions[j].value;

                const row = resultTableBody.insertRow();

                const productNameCell = row.insertCell(0);
                const properties = productName + "(" + sizeValue + ", " + colorValue + ")";
                productNameCell.textContent = properties;

                const quantityCell = row.insertCell(1);
                const quantityInput = document.createElement("input");
                quantityInput.type = "number";
                quantityInput.value = 1;
                quantityInput.className = "form-control";
                quantityCell.appendChild(quantityInput);

                const importPriceCell = row.insertCell(2);
                const importPriceInput = document.createElement("input");
                importPriceInput.type = "text";
                importPriceInput.id = "importPriceInput" + i + j;
                importPriceInput.placeholder = "Please Enter Import Price";
                importPriceInput.className = "form-control";
                importPriceInput.oninput = function () {
                    onInputImportPriceProductDetail(this.value, this.id);
                };

                const importPriceHidden = document.createElement("input");
                importPriceHidden.type = "hidden";
                importPriceHidden.id = "outputImportPrice" + i + j;
                importPriceHidden.className = "outputImportPrice";
                importPriceCell.appendChild(importPriceInput);
                importPriceCell.appendChild(importPriceHidden);

                const priceCell = row.insertCell(3);
                const priceInput = document.createElement("input");
                priceInput.type = "text";
                priceInput.id = "priceInput" + i + j;
                priceInput.placeholder = "Please Enter Price";
                priceInput.className = "form-control";
                priceInput.oninput = function () {
                    onInputPricePDOut(this.value, this.id);
                };

                const priceHidden = document.createElement("input");
                priceHidden.type = "hidden";
                priceHidden.id = "outputPrice" + i + j;
                priceHidden.className = "outputPrice";
                priceCell.appendChild(priceInput);
                priceCell.appendChild(priceHidden);

                const deleteCell = row.insertCell(4);
                const deleteButton = document.createElement("button");
                deleteButton.textContent = "Delete";
                deleteButton.className = "btn btn-danger";
                deleteButton.onclick = function () {
                    deleteRow(this);
                };
                deleteCell.appendChild(deleteButton);

                const sizeHidden = row.insertCell(5);
                sizeHidden.textContent = sizeValue;
                sizeHidden.style.display = "none";

                const colorHidden = row.insertCell(6);
                colorHidden.textContent = colorValue;
                colorHidden.style.display = "none";
            }
        }
    }

    function deleteRow(button) {
        const row = button.closest("tr");
        row.remove();
    }

    function getDataFromTable() {
        const resultArray = [];
        const tableRows = document.querySelectorAll('#result tbody tr');
        let isValid = true;

        tableRows.forEach(function (row) {
            const size = row.cells[5].textContent;
            const color = row.cells[6].textContent;
            const quantityInput = row.cells[1].querySelector("input").value;
            const importPrice = parseFloat(row.cells[2].querySelector(".outputImportPrice").value);
            const price = parseFloat(row.cells[3].querySelector(".outputPrice").value);

            const quantity = parseInt(quantityInput, 10);

            if (quantity <= 0 || importPrice <= 0 || price <= 0 || isNaN(importPrice) || isNaN(price)) {
                dangerAlert('Vui lòng điền đầy đủ thông tin bắt buộc.');
                isValid = false;
            }

            const variantData = {
                size: size,
                color: color,
                quantity: quantity,
                importPrice: importPrice,
                price: price
            };
            resultArray.push(variantData);
        });

        return isValid ? resultArray : null;
    }

    function addProductDetailAPI() {
        Swal.fire({
            title: "Bạn có muốn lưu những thay đổi?",
            showDenyButton: true,
            showCancelButton: true,
            confirmButtonText: "Lưu",
            denyButtonText: `Không lưu`
        }).then((result) => {
            if (result.isConfirmed) {
                const idProduct = document.getElementById("idProduct").value;
                const idMaterial = document.getElementById("material").value;
                const idOrigin = document.getElementById("origin").value;
                const idCategory = document.getElementById("category").value;
                const describe = document.getElementById("describeInput").value;
                const variantRequests = getDataFromTable();

                // Kiểm tra từng trường thông tin để hiển thị thông báo lỗi riêng lẻ
                if (!idProduct || !idMaterial || !idOrigin || !idCategory || !describe || !variantRequests) {
                    dangerAlert('Vui lòng điền đầy đủ thông tin bắt buộc.');
                    return;
                }

                const data = {
                    idProduct: idProduct,
                    idMaterial: idMaterial,
                    idOrigin: idOrigin,
                    idCategory: idCategory,
                    describe: describe,
                    variantRequests: variantRequests,
                };

                console.log(data); // Add this line to inspect the data being sent

                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: "http://localhost:8080/api/mangostore/admin/product-detail/add",
                    data: JSON.stringify(data),
                    dataType: 'json',
                    success: function (responseData) {
                        let addedDetails = responseData.added.join(', ');

                        if (responseData.added.length > 0) {
                            Swal.fire("Thêm thành công ").then(() => {
                                window.open("http://localhost:8080/mangostore/admin/product-detail", "_self");
                            });
                        } else {
                            Swal.fire("Không có sản phẩm nào được thêm mới vì trùng lặp.", "", "info");
                        }
                    },
                    error: function (e) {
                        console.log("LỖI : ", e);
                        Swal.fire("Lỗi!", "Không thể lưu thay đổi", "error");
                    }
                });
            } else if (result.isDenied) {
                Swal.fire("Những thay đổi không được lưu", "", "info");
            }
        });
    }



}
