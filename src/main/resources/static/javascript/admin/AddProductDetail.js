const createProductDetailPage = document.querySelector(".createProductDetail");
if (createProductDetailPage) {
    function generateVariants() {
        const productValue = document.getElementById("idProduct");
        const productName = productValue.options[productValue.selectedIndex].dataset.name;

        const sizeSelect = document.getElementById("size");
        const selectedSizeOptions = sizeSelect.selectedOptions;

        const colorSelect = document.getElementById("color");
        const selectedColorOptions = colorSelect.selectedOptions;

        if (!productName) {
            dangerAlert('Vui lòng chọn sản phẩm');
        } else if (selectedColorOptions.length === 0) {
            dangerAlert('Vui lòng chọn màu sản phẩm');
        } else if (selectedSizeOptions.length === 0) {
            dangerAlert('Vui lòng chọn size sản phẩm');
        } else {
            const resultTableBody = document.querySelector("#result tbody");
            resultTableBody.innerHTML = "";

            for (let i = 0; i < selectedSizeOptions.length; i++) {
                const sizeValue = selectedSizeOptions[i].value;

                for (let j = 0; j < selectedColorOptions.length; j++) {
                    const colorValue = selectedColorOptions[j].value;

                    const row = resultTableBody.insertRow();

                    const productNameCell = row.insertCell(0);
                    productNameCell.textContent = productName + "(" + sizeValue + ", " + colorValue + ")";

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
                    deleteButton.textContent = "Xóa";
                    deleteButton.className = "btn btn-danger delete-button";
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
    }

    function deleteRow(button) {
        const row = button.closest("tr");
        row.remove();
    }

    function getDataFromTable() {
        const resultArray = [];
        const tableRows = document.querySelectorAll('#result tbody tr');
        let isValid = true;
        let errorMessage = '';

        tableRows.forEach(function (row) {
            const size = row.cells[5].textContent;
            const color = row.cells[6].textContent;
            const quantityInput = row.cells[1].querySelector("input").value;
            const importPriceStr = row.cells[2].querySelector(".outputImportPrice").value;
            const priceStr = row.cells[3].querySelector(".outputPrice").value;

            const quantity = parseInt(quantityInput, 10);
            const importPrice = parseFloat(importPriceStr);
            const price = parseFloat(priceStr);

            if (quantityInput.trim() === '') {
                errorMessage = 'Số lượng không được để trống';
                isValid = false;
            } else if (isNaN(quantity)) {
                errorMessage = 'Số lượng phải là số hợp lệ';
                isValid = false;
            } else if (quantity <= 0) {
                errorMessage = 'Số lượng không thể nhỏ hơn hoặc bằng 0';
                isValid = false;
            } else if (importPriceStr.trim() === '') {
                errorMessage = 'Giá nhập không được để trống';
                isValid = false;
            } else if (isNaN(importPrice)) {
                errorMessage = 'Giá nhập phải là số hợp lệ';
                isValid = false;
            } else if (importPrice <= 0) {
                errorMessage = 'Giá nhập không thể nhỏ hơn hoặc bằng 0';
                isValid = false;
            } else if (priceStr.trim() === '') {
                errorMessage = 'Giá bán không được để trống';
                isValid = false;
            } else if (isNaN(price)) {
                errorMessage = 'Giá bán phải là số hợp lệ';
                isValid = false;
            } else if (price <= 0) {
                errorMessage = 'Giá bán không thể nhỏ hơn hoặc bằng 0';
                isValid = false;
            } else if (importPrice > price) {
                errorMessage = 'Giá nhập không thể lớn hơn giá bán';
                isValid = false;
            }

            if (isValid) {
                const variantData = {
                    size: size,
                    color: color,
                    quantity: quantity,
                    importPrice: importPrice,
                    price: price
                };
                resultArray.push(variantData);
            }
        });

        return {isValid, data: resultArray, errorMessage};
    }

    function addProductDetailAPI() {
        const validationResult = getDataFromTable();
        if (validationResult.isValid) {
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
                    const variantRequests = validationResult.data;
                    if (!idProduct || !idMaterial || !idOrigin || !idCategory || !describe) {
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
                            Swal.fire("Lỗi!", "Không thể lưu thay đổi", "error");
                        }
                    });
                } else if (result.isDenied) {
                    Swal.fire("Những thay đổi không được lưu", "", "info");
                }
            });
        } else {
            dangerAlert(validationResult.errorMessage);
        }
    }
}

const updateProductDetailPage = document.querySelector(".updateProductDetailPage");
if (updateProductDetailPage) {
    function updateProductDetail() {
        const formUpdate = document.getElementById('formUpdate');
        confirmAlertForm('Bạn có muốn cập nhật ?', 'Cập nhật thành công', formUpdate);
    }
}

const deleteProductDetailPage = document.querySelector(".deleteProductDetailPage");
if (deleteProductDetailPage) {
    function removeProductDetail() {
        const idProductDetail = document.getElementById("idProductDetail").value;
        const url = 'http://localhost:8080/mangostore/admin/product-detail/delete/' + idProductDetail;
        confirmAlertLink(event, 'Bạn có muốn xóa ?', 'Xóa thành công', url);
    }
}