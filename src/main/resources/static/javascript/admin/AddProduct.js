document.addEventListener('DOMContentLoaded', function () {
    const checkAddProductPage = document.querySelector(".checkAddProduct");
    if (checkAddProductPage) {
        const listImages = [];
        let imagesActive = "";

        function previewImages(event) {
            const preview = document.querySelector('#imagePreview');
            if (event.target.files) {
                [].forEach.call(event.target.files, readAndPreview);
            }

            function readAndPreview(file) {
                if (!/\.(jpe?g|png|gif)$/i.test(file.name)) {
                    return dangerAlert(file.name + " Không tồn tại");
                }
                const reader = new FileReader();
                reader.onload = function () {
                    const image = new Image();
                    image.height = 100;
                    image.title = file.name;
                    image.src = this.result;
                    image.className = 'me-1';
                    const card = document.createElement('div');
                    card.className = 'card m-2 image-card';
                    const cardBody = document.createElement('div');
                    cardBody.className = 'card-body p-2';
                    const removeButton = document.createElement('button');
                    removeButton.innerText = 'Xóa';
                    removeButton.className = 'btn btn-danger btn-sm mt-1 remove-btn';
                    cardBody.appendChild(image);
                    cardBody.appendChild(removeButton);
                    card.appendChild(cardBody);
                    preview.appendChild(card);
                    listImages.push(file.name);
                    card.onclick = function () {
                        document.querySelectorAll('.image-card').forEach(c => {
                            c.classList.remove('selected');
                            c.querySelector('.remove-btn').disabled = false;
                        });
                        card.classList.add('selected');
                        removeButton.disabled = true;
                        imagesActive = file.name;
                    };
                    removeButton.onclick = function (event) {
                        event.stopPropagation();
                        const index = listImages.indexOf(file.name);
                        if (index !== -1) {
                            listImages.splice(index, 1);
                        }
                        card.remove();
                        if (imagesActive === file.name) {
                            imagesActive = "";
                        }
                    };
                };
                reader.readAsDataURL(file);
            }
        }

        document.querySelector('#myFile').addEventListener("change", previewImages);

        async function createProduct(event) {
            const nameProductInput = document.getElementById('nameProductInput').value.trim();
            if (nameProductInput === '') {
                dangerAlert('Tên sản phẩm không được để trống');
                return;
            } else if (listImages.length === 0) {
                dangerAlert('Vui lòng thêm ảnh sản phẩm');
                return;
            } else if (imagesActive === '') {
                dangerAlert('Vui lòng chọn ảnh chính của sản phẩm');
                return;
            }

            try {
                const response = await fetch('/api/mangostore/admin/productsExistCreate/' + nameProductInput);
                if (response.ok) {
                    const data = await response.json();

                    if (data === 2) {
                        Swal.fire({
                            title: "Bạn có muốn lưu sản phẩm ?",
                            showDenyButton: true,
                            showCancelButton: true,
                            confirmButtonText: "Lưu",
                            denyButtonText: "Không lưu"
                        }).then((result) => {
                            if (result.isConfirmed) {
                                const data = {
                                    nameProduct: nameProductInput,
                                    listImages: listImages,
                                    imagesActive: imagesActive
                                };

                                $.ajax({
                                    type: "POST",
                                    contentType: "application/json",
                                    url: "http://localhost:8080/api/mangostore/admin/product/add",
                                    data: JSON.stringify(data),
                                    dataType: 'json',
                                    success: function (responseData) {
                                        Swal.fire("Lưu thành công!", "", "success").then(() => {
                                            window.open("http://localhost:8080/mangostore/admin/product", "_self");
                                        });
                                    },
                                    error: function (e) {
                                        Swal.fire("Lỗi", "Tạo sản phẩm không thành công!", "error");
                                    }
                                });
                            } else if (result.isDenied) {
                                Swal.fire("Những thay đổi không được lưu", "", "info");
                            }
                        });
                    } else {
                        Swal.fire("Tên sản phẩm đã tồn tại", "", "warning");
                    }
                } else {
                    throw new Error('Kiểm tra sản phẩm không thành công');
                }
            } catch (error) {
                Swal.fire("Lỗi", "Không kiểm tra được sự tồn tại của sản phẩm", "error");
            }
        }
        window.createProduct = createProduct;
    }
});


const listImagesUpdate = [];
const imageContainer = document.getElementById('imageContainer');
const newImagesInput = document.getElementById('newImagesInput');
let imagesActive = "";
const deletedImageIds = [];
const checkUpdateProductPage = document.querySelector('.checkUpdateProductPage');
if (checkUpdateProductPage) {
    document.addEventListener('DOMContentLoaded', function () {
        imageContainer.addEventListener('click', function (event) {
            const clickedCard = event.target.closest('.card');
            if (!clickedCard) return;

            const imageFile = clickedCard.querySelector('.product-image').getAttribute('src');
            const status = imageFile.includes('status=1');
            if (status) {
                clickedCard.classList.add('selected-image');
                imagesActive = imageFile;
            } else {
                imagesActive = "";
                imageContainer.querySelectorAll('.card').forEach(card => {
                    card.classList.remove('selected-image');
                });
            }
        });

        function previewImages(event) {
            const preview = document.querySelector('#imagePreview');
            preview.innerHTML = '';

            if (event.target.files) {
                [].forEach.call(event.target.files, readAndPreview);
            }

            function readAndPreview(file) {
                if (!/\.(jpe?g|png|gif)$/i.test(file.name)) {
                    return dangerAlert(file.name + " không phải là hình ảnh");
                }
                const reader = new FileReader();
                reader.onload = function () {
                    const image = new Image();
                    image.height = 100;
                    image.title = file.name;
                    image.src = this.result;
                    image.className = 'me-1';
                    const card = document.createElement('div');
                    card.className = 'card m-2 image-card';
                    const cardBody = document.createElement('div');
                    cardBody.className = 'card-body p-2';
                    const removeButton = document.createElement('button');
                    removeButton.innerText = 'Xóa';
                    removeButton.className = 'btn btn-danger btn-sm mt-1 remove-btn';
                    cardBody.appendChild(image);
                    cardBody.appendChild(removeButton);
                    card.appendChild(cardBody);
                    preview.appendChild(card);
                    listImagesUpdate.push(file.name);

                    card.onclick = function () {
                        document.querySelectorAll('.image-card').forEach(c => {
                            c.classList.remove('selected');
                            c.querySelector('.remove-btn').disabled = false;
                        });
                        card.classList.add('selected');
                        removeButton.disabled = true;
                        imagesActive = file.name;
                    };

                    removeButton.onclick = function (event) {
                        event.stopPropagation();
                        const index = listImagesUpdate.indexOf(file.name);
                        if (index !== -1) {
                            listImagesUpdate.splice(index, 1);
                        }
                        card.remove();
                        if (imagesActive === file.name) {
                            imagesActive = "";
                        }
                    };
                };
                reader.readAsDataURL(file);
            }
        }

        newImagesInput.addEventListener("change", previewImages);
        document.querySelector('#newImagesInput').addEventListener("change", previewImages);

        document.addEventListener('click', function (event) {
            if (event.target.classList.contains('delete-image-btn')) {
                const imageId = event.target.getAttribute('data-image-id');
                const imageFileName = event.target.getAttribute('data-image-file');
                deletedImageIds.push(imageId);
                const deletedImage = document.querySelector(`.product-image[src*="${imageFileName}"]`);
                if (deletedImage) {
                    deletedImage.parentNode.parentNode.remove();
                }
            }
        });
    });
}

async function updateProduct(event) {
    const nameProductInput = document.querySelector('input[name="nameProduct"]').value.trim();
    const idProduct = document.querySelector('input[name="id"]').value.trim();

    if (nameProductInput === '') {
        dangerAlert('Vui lòng nhập tên sản phẩm');
        return;
    }

    const data = {
        id: idProduct,
        nameProduct: nameProductInput,
        imagesToAdd: listImagesUpdate,
        imagesToDelete: deletedImageIds,
        activeImage: imagesActive
    };

    try {
        const url = '/api/mangostore/admin/productsExistUpdate/' + encodeURIComponent(nameProductInput) + '?id=' + encodeURIComponent(idProduct);
        const response = await fetch(url);
        if (response.ok) {
            const responseData = await response.json();
            if (responseData === 2 && idProduct !== '') {
                Swal.fire("Tên sản phẩm đã tồn tại", "", "warning");
            } else {
                Swal.fire({
                    title: "Bạn có muốn cập nhật sản phẩm ?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Cập nhật",
                    denyButtonText: "Không cập nhật"
                }).then((result) => {
                    if (result.isConfirmed) {
                        $.ajax({
                            type: "POST",
                            contentType: "application/json",
                            url: "http://localhost:8080/api/mangostore/admin/product/update",
                            data: JSON.stringify(data),
                            dataType: 'json',
                            success: function (responseData) {
                                Swal.fire("Cập nhật thành công!", "", "success").then(() => {
                                    window.open("http://localhost:8080/mangostore/admin/product", "_self");
                                });
                            },
                            error: function (e) {
                                Swal.fire("Lỗi", "Cập nhật sản phẩm không thành công!", "error");
                            }
                        });
                    } else if (result.isDenied) {
                        Swal.fire("Những thay đổi không được lưu", "", "info");
                    }
                });
            }
        } else {
            throw new Error('Kiểm tra sản phẩm không thành công');
        }
    } catch (error) {
        Swal.fire("Lỗi", "Không kiểm tra được sự tồn tại của sản phẩm", "error");
    }
}