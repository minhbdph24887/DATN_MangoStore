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
                    return alert(file.name + " is not an image");
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
                    removeButton.innerText = 'Remove';
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
                dangerAlert('Please Enter The Name Product');
                return;
            } else if (listImages.length === 0) {
                dangerAlert('Please add photos to the product');
                return;
            } else if (imagesActive === '') {
                dangerAlert('Please select the image of the product');
                return;
            }

            try {
                // Check if the product name already exists
                const response = await fetch('/api/mangostore/admin/productsExistCreate/' + nameProductInput);
                if (response.ok) {
                    const data = await response.json();

                    if (data === 2) { // If product does not exist, prompt to save changes
                        Swal.fire({
                            title: "Do you want to save changes?",
                            showDenyButton: true,
                            showCancelButton: true,
                            confirmButtonText: "Save",
                            denyButtonText: "Don't save"
                        }).then((result) => {
                            if (result.isConfirmed) {
                                // Proceed with form submission
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
                                        Swal.fire("Saved!", "", "success").then(() => {
                                            window.open("http://localhost:8080/mangostore/admin/product", "_self");
                                        });
                                    },
                                    error: function (e) {
                                        console.log("ERROR: ", e);
                                        Swal.fire("Error", "Creating product failed!", "error");
                                    }
                                });
                            } else if (result.isDenied) {
                                Swal.fire("Changes are not saved", "", "info");
                            }
                        });
                    } else {
                        // If product already exists, show a warning
                        Swal.fire("Product name already exists", "", "warning");
                    }
                } else {
                    throw new Error('Product check failed');
                }
            } catch (error) {
                console.error('Error checking product existence:', error);
                Swal.fire("Error", "Failed to check product existence", "error");
            }
        }

        // Gắn hàm createProduct vào đối tượng window để có thể truy cập từ HTML
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
            const status = imageFile.includes('status=1'); // Đoạn logic để xác định status của ảnh, sử dụng tên file để phân biệt

            if (status) {
                // Nếu ảnh có status = 0, làm nổi bật
                clickedCard.classList.add('selected-image');
                imagesActive = imageFile; // Lưu lại đường dẫn ảnh vào imagesActive
            } else {
                // Nếu người dùng chọn ảnh khác
                imagesActive = ""; // Xóa dữ liệu trong imagesActive
                // Xóa lớp 'selected-image' từ tất cả các card khác
                imageContainer.querySelectorAll('.card').forEach(card => {
                    card.classList.remove('selected-image');
                });
            }
        });

        function previewImages(event) {
            const preview = document.querySelector('#imagePreview');
            preview.innerHTML = ''; // Xóa các ảnh đã hiển thị trước đó

            if (event.target.files) {
                [].forEach.call(event.target.files, readAndPreview);
            }

            function readAndPreview(file) {
                if (!/\.(jpe?g|png|gif)$/i.test(file.name)) {
                    return alert(file.name + " is not an image");
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
                    removeButton.innerText = 'Remove';
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
                        imagesActive = file.name; // Đặt lại imagesActive khi người dùng chọn ảnh từ tập tin mới
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

        // Event listener to handle delete button clicks for existing images
        document.addEventListener('click', function (event) {
            if (event.target.classList.contains('delete-image-btn')) {
                const imageId = event.target.getAttribute('data-image-id');
                const imageFileName = event.target.getAttribute('data-image-file');

                // Add image ID to deletedImageIds array
                deletedImageIds.push(imageId);
                console.log('Deleted Image ID:', imageId);

                // Remove image from UI
                const deletedImage = document.querySelector(`.product-image[src*="${imageFileName}"]`);
                if (deletedImage) {
                    deletedImage.parentNode.parentNode.remove(); // Assuming card is the parent of card-body
                }
            }
        });
    });
}

async function updateProduct(event) {

    const nameProductInput = document.querySelector('input[name="nameProduct"]').value.trim();
    const idProduct = document.querySelector('input[name="id"]').value.trim();

    if (nameProductInput === '') {
        dangerAlert('Please Enter The Name Product');
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
        // Check if the product name already exists
        const url = '/api/mangostore/admin/productsExistUpdate/' + encodeURIComponent(nameProductInput) + '?id=' + encodeURIComponent(idProduct);
        const response = await fetch(url);
        if (response.ok) {
            const responseData = await response.json();

            if (responseData === 2 && idProduct !== '') {
                // If product already exists and idProduct is different, show a warning
                Swal.fire("Product name already exists", "", "warning");
            } else {
                // If product does not exist or is the same, prompt to save changes
                Swal.fire({
                    title: "Do you want to save changes?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Save",
                    denyButtonText: "Don't save"
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Proceed with form submission
                        $.ajax({
                            type: "POST",
                            contentType: "application/json",
                            url: "http://localhost:8080/api/mangostore/admin/product/update",
                            data: JSON.stringify(data),
                            dataType: 'json',
                            success: function (responseData) {
                                Swal.fire("Saved!", "", "success").then(() => {
                                    window.open("http://localhost:8080/mangostore/admin/product", "_self");
                                });
                            },
                            error: function (e) {
                                console.log("ERROR: ", e);
                                Swal.fire("Error", "Updating product failed!", "error");
                            }
                        });
                    } else if (result.isDenied) {
                        Swal.fire("Changes are not saved", "", "info");
                    }
                });
            }
        } else {
            throw new Error('Product check failed');
        }
    } catch (error) {
        console.error('Error checking product existence:', error);
        Swal.fire("Error", "Failed to check product existence", "error");
    }
}