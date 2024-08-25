function confirmAndSubmitColor(event) {
    event.preventDefault();

    const colorName = document.getElementsByName('nameColor')[0].value.trim();

    if (colorName === '') {
        Swal.fire("Lỗi", "Tên màu không được để trống", "error");
        return false;
    }else if(/\d/.test(colorName)){
        Swal.fire("Lỗi", "Tên màu không được có số", "error");
        return false;
    }

    fetch('/api/mangostore/admin/colorsExistCreat/' + colorName)
        .then(function (response) {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Kiểm tra màu không thành công');
            }
        })
        .then(function (data) {
            if (data === 2) {
                Swal.fire({
                    title: "Bạn muốn lưu những gì đã thay đổi?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Có",
                    denyButtonText: `Không`
                }).then((result) => {
                    if (result.isConfirmed) {
                        document.getElementById('addColorForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Những thay đổi không được lưu", "", "info");
                    }
                });
            } else {
                Swal.fire("Màu đã tồn tại", "", "warning");
            }
        })
        .catch(function (error) {
            Swal.fire("Lỗi", "Không kiểm tra được sự tồn tại của màu sắc", "error");
        });

    return false;
}


function confirmAndSubmitUpdateColor(event) {
    event.preventDefault();

    const colorName = document.getElementsByName('nameColor')[0].value.trim();
    const codeColor = document.getElementById('codeColor').value.trim();
    if (colorName === '') {
        Swal.fire("Lỗi", "Tên màu không thể trống", "error");
        return false;
    }else if(/\d/.test(colorName)){
        Swal.fire("Lỗi", "Tên màu không được có số", "error");
        return false;
    }

    const url = '/api/mangostore/admin/colorsExistUpdate/' + encodeURIComponent(colorName) + '?codeColor=' + encodeURIComponent(codeColor);

    fetch(url)
        .then(function (response) {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Kiểm tra màu không thành công');
            }
        })
        .then(function (data) {
            if (data === 2 && codeColor !== '') {
                Swal.fire("Màu này đã tồn tại", "", "warning");
            } else {
                Swal.fire({
                    title: "Bạn muốn lưu những gì đã thay đổi?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Có",
                    denyButtonText: `Không`
                }).then((result) => {
                    if (result.isConfirmed) {
                        document.getElementById('updateColorForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Những thay đổi không được lưu", "", "info");
                    }
                });
            }
        })
        .catch(function (error) {
            Swal.fire("Lỗi", "Không kiểm tra được sự tồn tại của màu sắc", "error");
        });

    return false;
}


function removeColor(event) {
    event.preventDefault();

    Swal.fire({
        title: "Bạn có chắc chắn muốn xóa màu này không ?",
        showCancelButton: true,
        confirmButtonText: "Có",
        cancelButtonText: "Không",
        icon: "warning"
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = event.target.closest('a').href;
        }
    });

    return false;
}

function confirmAndSubmitOrigin(event) {
    event.preventDefault();
    const originName = document.getElementsByName('nameOrigin')[0].value.trim();
    if (originName === '') {
        Swal.fire("Lỗi", "Tên nhà sản xuất không được để trống", "error");
        return false;
    }else if(/\d/.test(originName)){
        Swal.fire("Lỗi", "Tên nhà sản xuất không được có số", "error");
        return false;
    }

    fetch('/api/mangostore/admin/originsExistCreate/' + originName)
        .then(function (response) {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Kiểm tra nguồn gốc không thành công');
            }
        })
        .then(function (data) {
            if (data === 2) {
                Swal.fire({
                    title: "Bạn muốn lưu những gì đã thay đổi?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Có",
                    denyButtonText: `Không`
                }).then((result) => {
                    if (result.isConfirmed) {
                        document.getElementById('addOriginForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Những thay đổi không được lưu", "", "info");
                    }
                });
            } else {
                Swal.fire("Nhà sản xuất đã tồn tại", "", "warning");
            }
        })
        .catch(function (error) {
            Swal.fire("Lỗi", "Không kiểm tra được sự tồn tại của nguồn gốc", "error");
        });

    return false;
}

function confirmAndSubmitUpdateOrigin(event) {
    event.preventDefault();

    const originName = document.getElementsByName('nameOrigin')[0].value.trim();
    const codeOrigin = document.getElementById('codeOrigin').value.trim();
    if (originName === '') {
        Swal.fire("Lỗi", "Tên nhà sản xuất không được để trống", "error");
        return false;
    }else if(/\d/.test(originName)){
        Swal.fire("Lỗi", "Tên nhà sản xuất không được có số", "error");
        return false;
    }

    const url = '/api/mangostore/admin/originsExistUpdate/' + encodeURIComponent(originName) + '?codeOrigin=' + encodeURIComponent(codeOrigin);

    fetch(url)
        .then(function (response) {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Kiểm tra nguồn gốc không thành công');
            }
        })
        .then(function (data) {
            if (data === 2 && codeOrigin !== '') {
                Swal.fire("Nhà sản xuất đã tồn tại", "", "warning");
            } else {
                Swal.fire({
                    title: "Bạn có muốn lưu những gì thay đổi?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Có",
                    denyButtonText: `Không`
                }).then((result) => {
                    if (result.isConfirmed) {
                        document.getElementById('updateOriginForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Những thay đổi không được lưu", "", "info");
                    }
                });
            }
        })
        .catch(function (error) {
            Swal.fire("Lỗi", "Không kiểm tra được sự tồn tại của nguồn gốc", "error");
        });

    return false;
}

function removeOrigin(event) {
    event.preventDefault();

    Swal.fire({
        title: "Bạn có muốn xóa nhà sản xuất này ?",
        showCancelButton: true,
        confirmButtonText: "Có",
        cancelButtonText: "Không",
        icon: "warning"
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = event.target.closest('a').href;
        }
    });

    return false;
}

function confirmAndSubmitSize(event) {
    event.preventDefault();

    const sizeName = document.getElementsByName('nameSize')[0].value.trim();

    if (sizeName === '') {
        Swal.fire("Lỗi", "Tên kích cỡ không được để trống", "error");
        return false;
    }

    fetch('/api/mangostore/admin/sizesExistCreate/' + sizeName)
        .then(function (response) {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Size check failed');
            }
        })
        .then(function (data) {
            if (data === 2) {
                Swal.fire({
                    title: "Bạn có muốn lưu những gì thay đổi ?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Có",
                    denyButtonText: `Không`
                }).then((result) => {
                    if (result.isConfirmed) {
                        document.getElementById('addSizeForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Những thay đổi không được lưu", "", "info");
                    }
                });
            } else {
                Swal.fire("Kích cỡ đã tồn tại", "", "warning");
            }
        })
        .catch(function (error) {
            Swal.fire("Lỗi", "Không kiểm tra được sự tồn tại của kích thước", "error");
        });

    return false;
}

function confirmAndSubmitUpdateSize(event) {
    event.preventDefault();

    const sizeName = document.getElementsByName('nameSize')[0].value.trim();
    const codeSize = document.getElementById('codeSize').value.trim();
    if (sizeName === '') {
        Swal.fire("Lỗi", "Tên kích cỡ không được để trống", "error");
        return false;
    }
    const url = '/api/mangostore/admin/sizesExistUpdate/' + encodeURIComponent(sizeName) + '?codeSize=' + encodeURIComponent(codeSize);

    fetch(url)
        .then(function (response) {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Kiểm tra kích thước không thành công');
            }
        })
        .then(function (data) {
            if (data === 2 && codeSize !== '') {
                Swal.fire("Kích cỡ đã tồn tại", "", "warning");
            } else {
                Swal.fire({
                    title: "Bạn có muốn lưu những gì thay đổi ?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Có",
                    denyButtonText: `Không`
                }).then((result) => {
                    if (result.isConfirmed) {
                        document.getElementById('updateSizeForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Những thay đổi không được lưu", "", "info");
                    }
                });
            }
        })
        .catch(function (error) {
            Swal.fire("Lỗi", "Không kiểm tra được sự tồn tại của kích thước", "error");
        });

    return false;
}

function removeSize(event) {
    event.preventDefault();

    Swal.fire({
        title: "Bạn có muốn xóa kích cỡ này ?",
        showCancelButton: true,
        confirmButtonText: "Có",
        cancelButtonText: "Không",
        icon: "warning"
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = event.target.closest('a').href;
        }
    });

    return false;
}

function confirmAndSubmitMaterial(event) {
    event.preventDefault();

    const materialName = document.getElementsByName('nameMaterial')[0].value.trim();
    if (materialName === '') {
        Swal.fire("Lỗi", "Tên chất liệu không được để trống", "error");
        return false;
    }else if(/\d/.test(materialName)){
        Swal.fire("Lỗi", "Tên chất liệu không được có số", "error");
        return false;
    }


    fetch('/api/mangostore/admin/materialsExistCreate/' + materialName)
        .then(function (response) {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Kiểm tra chất liệu không thành công');
            }
        })
        .then(function (data) {
            if (data === 2) {
                Swal.fire({
                    title: "Bạn có muốn lưu lại những gì thay đổi ?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Có",
                    denyButtonText: `Không`
                }).then((result) => {
                    if (result.isConfirmed) {
                        document.getElementById('addMaterialForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Những thay đổi không được lưu", "", "info");
                    }
                });
            } else {
                Swal.fire("Chất liệu đã tồn tại", "", "warning");
            }
        })
        .catch(function (error) {
            Swal.fire("Lỗi", "Không kiểm tra được sự tồn tại của chất liệu", "error");
        });

    return false;
}


function confirmAndSubmitUpdateMaterial(event) {
    event.preventDefault();

    const materialName = document.getElementsByName('nameMaterial')[0].value.trim();
    const codeMaterial = document.getElementById('codeMaterial').value.trim();
    if (materialName === '') {
        Swal.fire("Lỗi", "Tên chất liệu không được để trống", "error");
        return false;
    }else if(/\d/.test(materialName)){
        Swal.fire("Lỗi", "Tên chất liệu không được có số", "error");
        return false;
    }


    const url = '/api/mangostore/admin/materialsExistUpdate/' + encodeURIComponent(materialName) + '?codeMaterial=' + encodeURIComponent(codeMaterial);

    fetch(url)
        .then(function (response) {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Kiểm tra chất liệu không thành công');
            }
        })
        .then(function (data) {
            if (data === 2 && codeMaterial !== '') {
                Swal.fire("Chất liệu đã tồn tại", "", "warning");
            } else {
                Swal.fire({
                    title: "Bạn có muốn lưu lại những gì đã thay đổi ?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Có",
                    denyButtonText: `Không`
                }).then((result) => {
                    if (result.isConfirmed) {
                        document.getElementById('updateMaterialForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Những thay đổi không được lưu", "", "info");
                    }
                });
            }
        })
        .catch(function (error) {
            Swal.fire("Lỗi", "Không kiểm tra được sự tồn tại của chất liệu", "error");
        });

    return false;
}

function removeMaterial(event) {
    event.preventDefault();

    Swal.fire({
        title: "Bạn có muốn xóa chất liệu này không ?",
        showCancelButton: true,
        confirmButtonText: "Có",
        cancelButtonText: "Không",
        icon: "warning"
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = event.target.closest('a').href;
        }
    });

    return false;
}

function confirmAndSubmitCategory(event) {
    event.preventDefault();

    const categoryName = document.getElementsByName('nameCategory')[0].value.trim();

    if (categoryName === '') {
        Swal.fire("Lỗi", "Tên danh mục không được để trống", "error");
        return false;
    }else if(/\d/.test(categoryName)){
        Swal.fire("Lỗi", "Tên danh mục không được có số", "error");
        return false;
    }

    fetch('/api/mangostore/admin/categoriesExistCreate/' + categoryName)
        .then(function (response) {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Kiểm tra danh mục không thành công');
            }
        })
        .then(function (data) {
            if (data === 2) {
                Swal.fire({
                    title: "Bạn có muốn lưu lại những gì đã thay đổi ?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Có",
                    denyButtonText: `Không`
                }).then((result) => {
                    if (result.isConfirmed) {
                        document.getElementById('addCategoryForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Những thay đổi không được lưu", "", "info");
                    }
                });
            } else {
                Swal.fire("Danh mục đã tồn tại", "", "warning");
            }
        })
        .catch(function (error) {
            Swal.fire("Lỗi", "Không kiểm tra được sự tồn tại của danh mục", "error");
        });

    return false;
}

function confirmAndSubmitUpdateCategory(event) {
    event.preventDefault();

    const categoryName = document.getElementsByName('nameCategory')[0].value.trim();
    const codeCategory = document.getElementById('codeCategory').value.trim();
    if (categoryName === '') {
        Swal.fire("Lỗi", "Tên danh mục không được để trống", "error");
        return false;
    }else if(/\d/.test(categoryName)){
        Swal.fire("Lỗi", "Tên danh mục không được có số", "error");
        return false;
    }

    const url = '/api/mangostore/admin/categoriesExistUpdate/' + encodeURIComponent(categoryName) + '?codeCategory=' + encodeURIComponent(codeCategory);

    fetch(url)
        .then(function (response) {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Kiểm tra danh mục không thành công');
            }
        })
        .then(function (data) {
            if (data === 2 && codeCategory !== '') {
                Swal.fire("Danh mục đã tồn tại", "", "warning");
            } else {
                Swal.fire({
                    title: "Bạn có muốn lưu lại những gì đã thay đổi ?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Có",
                    denyButtonText: `Không`
                }).then((result) => {
                    if (result.isConfirmed) {
                        document.getElementById('updateCategoryForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Những thay đổi không được lưu", "", "info");
                    }
                });
            }
        })
        .catch(function (error) {
            Swal.fire("Lỗi", "Không kiểm tra được sự tồn tại của danh mục", "error");
        });

    return false;
}

function removeCategory(event) {
    event.preventDefault();

    Swal.fire({
        title: "Bạn có muốn xóa danh mục này ?",
        showCancelButton: true,
        confirmButtonText: "Có",
        cancelButtonText: "Không",
        icon: "warning"
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = event.target.closest('a').href;
        }
    });

    return false;
}

const deleteProductPage = document.querySelector('.deleteProductPage');
if (deleteProductPage) {
    function removeProduct() {
        const idProduct = document.getElementById('id').value;
        const url = 'http://localhost:8080/mangostore/admin/product/delete/' + idProduct;
        confirmAlertLink(event, 'Bạn có muốn xóa sản phẩm này không ?', 'Xóa thành công', url);
    }
}