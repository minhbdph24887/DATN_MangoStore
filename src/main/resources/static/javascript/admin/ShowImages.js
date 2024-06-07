const showImagesPage = document.querySelector('.showImage');
if (showImagesPage) {
    const originalImageSrc = document.getElementById('previewImage').src;
    document.getElementById('imageInput').addEventListener('change', function (e) {
        const reader = new FileReader();
        reader.onload = function (e) {
            document.getElementById('previewImage').src = e.target.result;
        }
        reader.readAsDataURL(this.files[0]);
    });

    document.querySelector('.btn.btn-default.md-btn-flat').addEventListener('click', function () {
        document.getElementById('previewImage').src = originalImageSrc;
    });
}