function confirmAndSubmitColor(event) {
    event.preventDefault(); // Ngăn chặn gửi form mặc định

    var colorName = document.getElementsByName('nameColor')[0].value.trim();

    // Kiểm tra nếu tên màu trống
    if (colorName === '') {
        Swal.fire("Error", "Color name cannot be empty", "error");
        return false; // Ngăn chặn gửi form
    }

    // Gửi yêu cầu AJAX để kiểm tra màu tồn tại
    fetch('/api/mangostore/admin/colorsExistCreat/' + colorName)
        .then(function(response) {
            if (response.ok) {
                return response.json(); // Phân tích phản hồi thành JSON
            } else {
                throw new Error('Color check failed');
            }
        })
        .then(function(data) {
            // Xử lý dữ liệu phản hồi
            if(data === 2) {
                // Nếu màu chưa tồn tại, hiển thị tùy chọn lưu
                Swal.fire({
                    title: "Do you want to save the changes?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Save",
                    denyButtonText: `Don't save`
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Tiến hành gửi form đi
                        document.getElementById('addColorForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Changes are not saved", "", "info");
                    }
                });
            }else {
                // Nếu màu đã tồn tại, hiển thị cảnh báo
                Swal.fire("Color already exists", "", "warning");
            }
        })
        .catch(function(error) {
            console.error('Error checking color existence:', error);
            // Xử lý lỗi theo nhu cầu
            Swal.fire("Error", "Failed to check color existence", "error");
        });

    return false; // Ngăn chặn gửi form mặc định
}


function confirmAndSubmitUpdateColor(event) {
    event.preventDefault(); // Ngăn chặn gửi form mặc định

    var colorName = document.getElementsByName('nameColor')[0].value.trim();
    var codeColor = document.getElementById('codeColor').value.trim(); // Lấy giá trị codeColor từ hidden input
    // Kiểm tra nếu tên màu trống
    if (colorName === '') {
        Swal.fire("Error", "Color name cannot be empty", "error");
        return false; // Ngăn chặn gửi form
    }

    // Gửi yêu cầu AJAX để kiểm tra màu tồn tại
    var url = '/api/mangostore/admin/colorsExistUpdate/' + encodeURIComponent(colorName) + '?codeColor=' + encodeURIComponent(codeColor);

    console.log(url)

    fetch(url)
        .then(function(response) {
            if (response.ok) {
                return response.json(); // Phân tích phản hồi thành JSON
            } else {
                throw new Error('Color check failed');
            }
        })
        .then(function(data) {
            // Xử lý dữ liệu phản hồi
            if (data === 2 && codeColor !== '') {
                // Nếu màu đã tồn tại và codeColor khác rỗng, hiển thị cảnh báo
                Swal.fire("Color already exists", "", "warning");
            } else {
                // Nếu màu chưa tồn tại hoặc là trường hợp update với codeColor rỗng, hiển thị tùy chọn lưu
                Swal.fire({
                    title: "Do you want to save the changes?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Save",
                    denyButtonText: `Don't save`
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Tiến hành gửi form đi
                        document.getElementById('updateColorForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Changes are not saved", "", "info");
                    }
                });
            }
        })
        .catch(function(error) {
            console.error('Error checking color existence:', error);
            // Xử lý lỗi theo nhu cầu
            Swal.fire("Error", "Failed to check color existence", "error");
        });

    return false; // Ngăn chặn gửi form mặc định
}


function removeColor(event) {
    event.preventDefault(); // Prevent the default anchor click behavior

    Swal.fire({
        title: "Are you sure you want to delete this color?",
        showCancelButton: true,
        confirmButtonText: "Delete",
        cancelButtonText: "Cancel",
        icon: "warning"
    }).then((result) => {
        if (result.isConfirmed) {
            // Redirect to the delete URL
            window.location.href = event.target.closest('a').href;
        }
    });

    return false; // Prevent the default action
}

<!-- Add this JavaScript in a file named originManagement.js -->

function confirmAndSubmitOrigin(event) {
    event.preventDefault(); // Ngăn chặn gửi form mặc định

    var originName = document.getElementsByName('nameOrigin')[0].value.trim();

    // Kiểm tra nếu tên nguồn trống
    if (originName === '') {
        Swal.fire("Error", "Origin name cannot be empty", "error");
        return false; // Ngăn chặn gửi form
    }

    // Gửi yêu cầu AJAX để kiểm tra nguồn tồn tại
    fetch('/api/mangostore/admin/originsExistCreate/' + originName)
        .then(function(response) {
            if (response.ok) {
                return response.json(); // Phân tích phản hồi thành JSON
            } else {
                throw new Error('Origin check failed');
            }
        })
        .then(function(data) {
            // Xử lý dữ liệu phản hồi
            if(data === 2) {
                // Nếu nguồn chưa tồn tại, hiển thị tùy chọn lưu
                Swal.fire({
                    title: "Do you want to save the changes?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Save",
                    denyButtonText: `Don't save`
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Tiến hành gửi form đi
                        document.getElementById('addOriginForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Changes are not saved", "", "info");
                    }
                });
            } else {
                // Nếu nguồn đã tồn tại, hiển thị cảnh báo
                Swal.fire("Origin already exists", "", "warning");
            }
        })
        .catch(function(error) {
            console.error('Error checking origin existence:', error);
            // Xử lý lỗi theo nhu cầu
            Swal.fire("Error", "Failed to check origin existence", "error");
        });

    return false; // Ngăn chặn gửi form mặc định
}

function confirmAndSubmitUpdateOrigin(event) {
    event.preventDefault(); // Ngăn chặn gửi form mặc định

    var originName = document.getElementsByName('nameOrigin')[0].value.trim();
    var codeOrigin = document.getElementById('codeOrigin').value.trim(); // Lấy giá trị codeOrigin từ hidden input
    // Kiểm tra nếu tên nguồn trống
    if (originName === '') {
        Swal.fire("Error", "Origin name cannot be empty", "error");
        return false; // Ngăn chặn gửi form
    }

    // Gửi yêu cầu AJAX để kiểm tra nguồn tồn tại
    var url = '/api/mangostore/admin/originsExistUpdate/' + encodeURIComponent(originName) + '?codeOrigin=' + encodeURIComponent(codeOrigin);

    console.log(url);

    fetch(url)
        .then(function(response) {
            if (response.ok) {
                return response.json(); // Phân tích phản hồi thành JSON
            } else {
                throw new Error('Origin check failed');
            }
        })
        .then(function(data) {
            // Xử lý dữ liệu phản hồi
            if (data === 2 && codeOrigin !== '') {
                // Nếu nguồn đã tồn tại và codeOrigin khác rỗng, hiển thị cảnh báo
                Swal.fire("Origin already exists", "", "warning");
            } else {
                // Nếu nguồn chưa tồn tại hoặc là trường hợp update với codeOrigin rỗng, hiển thị tùy chọn lưu
                Swal.fire({
                    title: "Do you want to save the changes?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Save",
                    denyButtonText: `Don't save`
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Tiến hành gửi form đi
                        document.getElementById('updateOriginForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Changes are not saved", "", "info");
                    }
                });
            }
        })
        .catch(function(error) {
            console.error('Error checking origin existence:', error);
            // Xử lý lỗi theo nhu cầu
            Swal.fire("Error", "Failed to check origin existence", "error");
        });

    return false; // Ngăn chặn gửi form mặc định
}




// Function to confirm origin deletion
function removeOrigin(event) {
    event.preventDefault(); // Prevent the default anchor click behavior

    Swal.fire({
        title: "Are you sure you want to delete this origin?",
        showCancelButton: true,
        confirmButtonText: "Delete",
        cancelButtonText: "Cancel",
        icon: "warning"
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = event.target.closest('a').href;
        }
    });

    return false; // Prevent the default action
}

// Function to confirm size (or origin) deletion
// Function to handle size (or origin) addition
function confirmAndSubmitSize(event) {
    event.preventDefault(); // Ngăn chặn gửi form mặc định

    var sizeName = document.getElementsByName('nameSize')[0].value.trim();

    // Kiểm tra nếu tên kích thước trống
    if (sizeName === '') {
        Swal.fire("Error", "Size name cannot be empty", "error");
        return false; // Ngăn chặn gửi form
    }

    // Gửi yêu cầu AJAX để kiểm tra kích thước tồn tại
    fetch('/api/mangostore/admin/sizesExistCreate/' + sizeName)
        .then(function(response) {
            if (response.ok) {
                return response.json(); // Phân tích phản hồi thành JSON
            } else {
                throw new Error('Size check failed');
            }
        })
        .then(function(data) {
            // Xử lý dữ liệu phản hồi
            if(data === 2) {
                // Nếu kích thước chưa tồn tại, hiển thị tùy chọn lưu
                Swal.fire({
                    title: "Do you want to save the changes?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Save",
                    denyButtonText: `Don't save`
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Tiến hành gửi form đi
                        document.getElementById('addSizeForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Changes are not saved", "", "info");
                    }
                });
            } else {
                // Nếu kích thước đã tồn tại, hiển thị cảnh báo
                Swal.fire("Size already exists", "", "warning");
            }
        })
        .catch(function(error) {
            console.error('Error checking size existence:', error);
            // Xử lý lỗi theo nhu cầu
            Swal.fire("Error", "Failed to check size existence", "error");
        });

    return false; // Ngăn chặn gửi form mặc định
}

function confirmAndSubmitUpdateSize(event) {
    event.preventDefault(); // Ngăn chặn gửi form mặc định

    var sizeName = document.getElementsByName('nameSize')[0].value.trim();
    var codeSize = document.getElementById('codeSize').value.trim(); // Lấy giá trị codeSize từ hidden input
    // Kiểm tra nếu tên kích thước trống
    if (sizeName === '') {
        Swal.fire("Error", "Size name cannot be empty", "error");
        return false; // Ngăn chặn gửi form
    }

    // Gửi yêu cầu AJAX để kiểm tra kích thước tồn tại
    var url = '/api/mangostore/admin/sizesExistUpdate/' + encodeURIComponent(sizeName) + '?codeSize=' + encodeURIComponent(codeSize);

    console.log(url);

    fetch(url)
        .then(function(response) {
            if (response.ok) {
                return response.json(); // Phân tích phản hồi thành JSON
            } else {
                throw new Error('Size check failed');
            }
        })
        .then(function(data) {
            // Xử lý dữ liệu phản hồi
            if (data === 2 && codeSize !== '') {
                // Nếu kích thước đã tồn tại và codeSize khác rỗng, hiển thị cảnh báo
                Swal.fire("Size already exists", "", "warning");
            } else {
                // Nếu kích thước chưa tồn tại hoặc là trường hợp update với codeSize rỗng, hiển thị tùy chọn lưu
                Swal.fire({
                    title: "Do you want to save the changes?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Save",
                    denyButtonText: `Don't save`
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Tiến hành gửi form đi
                        document.getElementById('updateSizeForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Changes are not saved", "", "info");
                    }
                });
            }
        })
        .catch(function(error) {
            console.error('Error checking size existence:', error);
            // Xử lý lỗi theo nhu cầu
            Swal.fire("Error", "Failed to check size existence", "error");
        });

    return false; // Ngăn chặn gửi form mặc định
}




// Function to confirm size (or origin) deletion
function removeSize(event) {
    event.preventDefault(); // Prevent the default anchor click behavior

    Swal.fire({
        title: "Are you sure you want to delete this size (or origin)?",
        showCancelButton: true,
        confirmButtonText: "Delete",
        cancelButtonText: "Cancel",
        icon: "warning"
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = event.target.closest('a').href;
        }
    });

    return false; // Prevent the default action
}

// Function to handle material addition
function confirmAndSubmitMaterial(event) {
    event.preventDefault(); // Ngăn chặn gửi form mặc định

    var materialName = document.getElementsByName('nameMaterial')[0].value.trim();

    // Kiểm tra nếu tên chất liệu trống
    if (materialName === '') {
        Swal.fire("Error", "Material name cannot be empty", "error");
        return false; // Ngăn chặn gửi form
    }

    // Gửi yêu cầu AJAX để kiểm tra chất liệu tồn tại
    fetch('/api/mangostore/admin/materialsExistCreate/' + materialName)
        .then(function(response) {
            if (response.ok) {
                return response.json(); // Phân tích phản hồi thành JSON
            } else {
                throw new Error('Material check failed');
            }
        })
        .then(function(data) {
            // Xử lý dữ liệu phản hồi
            if(data === 2) {
                // Nếu chất liệu chưa tồn tại, hiển thị tùy chọn lưu
                Swal.fire({
                    title: "Do you want to save the changes?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Save",
                    denyButtonText: `Don't save`
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Tiến hành gửi form đi
                        document.getElementById('addMaterialForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Changes are not saved", "", "info");
                    }
                });
            } else {
                // Nếu chất liệu đã tồn tại, hiển thị cảnh báo
                Swal.fire("Material already exists", "", "warning");
            }
        })
        .catch(function(error) {
            console.error('Error checking material existence:', error);
            // Xử lý lỗi theo nhu cầu
            Swal.fire("Error", "Failed to check material existence", "error");
        });

    return false; // Ngăn chặn gửi form mặc định
}


function confirmAndSubmitUpdateMaterial(event) {
    event.preventDefault(); // Ngăn chặn gửi form mặc định

    var materialName = document.getElementsByName('nameMaterial')[0].value.trim();
    var codeMaterial = document.getElementById('codeMaterial').value.trim(); // Lấy giá trị codeMaterial từ hidden input
    // Kiểm tra nếu tên chất liệu trống
    if (materialName === '') {
        Swal.fire("Error", "Material name cannot be empty", "error");
        return false; // Ngăn chặn gửi form
    }

    // Gửi yêu cầu AJAX để kiểm tra chất liệu tồn tại
    var url = '/api/mangostore/admin/materialsExistUpdate/' + encodeURIComponent(materialName) + '?codeMaterial=' + encodeURIComponent(codeMaterial);

    console.log(url)

    fetch(url)
        .then(function(response) {
            if (response.ok) {
                return response.json(); // Phân tích phản hồi thành JSON
            } else {
                throw new Error('Material check failed');
            }
        })
        .then(function(data) {
            // Xử lý dữ liệu phản hồi
            if (data === 2 && codeMaterial !== '') {
                // Nếu chất liệu đã tồn tại và codeMaterial khác rỗng, hiển thị cảnh báo
                Swal.fire("Material already exists", "", "warning");
            } else {
                // Nếu chất liệu chưa tồn tại hoặc là trường hợp update với codeMaterial rỗng, hiển thị tùy chọn lưu
                Swal.fire({
                    title: "Do you want to save the changes?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Save",
                    denyButtonText: `Don't save`
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Tiến hành gửi form đi
                        document.getElementById('updateMaterialForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Changes are not saved", "", "info");
                    }
                });
            }
        })
        .catch(function(error) {
            console.error('Error checking material existence:', error);
            // Xử lý lỗi theo nhu cầu
            Swal.fire("Error", "Failed to check material existence", "error");
        });

    return false; // Ngăn chặn gửi form mặc định
}


// Function to confirm material deletion
function removeMaterial (event) {
    event.preventDefault(); // Prevent the default anchor click behavior

    Swal.fire({
        title: "Are you sure you want to delete this material?",
        showCancelButton: true,
        confirmButtonText: "Delete",
        cancelButtonText: "Cancel",
        icon: "warning"
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = event.target.closest('a').href;
        }
    });

    return false; // Prevent the default action
}

// Function to handle category addition
function confirmAndSubmitCategory(event) {
    event.preventDefault(); // Ngăn chặn gửi form mặc định

    var categoryName = document.getElementsByName('nameCategory')[0].value.trim();

    // Kiểm tra nếu tên danh mục trống
    if (categoryName === '') {
        Swal.fire("Error", "Category name cannot be empty", "error");
        return false; // Ngăn chặn gửi form
    }

    // Gửi yêu cầu AJAX để kiểm tra danh mục tồn tại
    fetch('/api/mangostore/admin/categoriesExistCreate/' + categoryName)
        .then(function(response) {
            if (response.ok) {
                return response.json(); // Phân tích phản hồi thành JSON
            } else {
                throw new Error('Category check failed');
            }
        })
        .then(function(data) {
            // Xử lý dữ liệu phản hồi
            if (data === 2) {
                // Nếu danh mục chưa tồn tại, hiển thị tùy chọn lưu
                Swal.fire({
                    title: "Do you want to save the changes?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Save",
                    denyButtonText: `Don't save`
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Tiến hành gửi form đi
                        document.getElementById('addCategoryForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Changes are not saved", "", "info");
                    }
                });
            } else {
                // Nếu danh mục đã tồn tại, hiển thị cảnh báo
                Swal.fire("Category already exists", "", "warning");
            }
        })
        .catch(function(error) {
            console.error('Error checking category existence:', error);
            // Xử lý lỗi theo nhu cầu
            Swal.fire("Error", "Failed to check category existence", "error");
        });

    return false; // Ngăn chặn gửi form mặc định
}

function confirmAndSubmitUpdateCategory(event) {
    event.preventDefault(); // Ngăn chặn gửi form mặc định

    var categoryName = document.getElementsByName('nameCategory')[0].value.trim();
    var codeCategory = document.getElementById('codeCategory').value.trim(); // Lấy giá trị codeCategory từ hidden input
    // Kiểm tra nếu tên danh mục trống
    if (categoryName === '') {
        Swal.fire("Error", "Category name cannot be empty", "error");
        return false; // Ngăn chặn gửi form
    }

    // Gửi yêu cầu AJAX để kiểm tra danh mục tồn tại
    var url = '/api/mangostore/admin/categoriesExistUpdate/' + encodeURIComponent(categoryName) + '?codeCategory=' + encodeURIComponent(codeCategory);

    console.log(url);

    fetch(url)
        .then(function(response) {
            if (response.ok) {
                return response.json(); // Phân tích phản hồi thành JSON
            } else {
                throw new Error('Category check failed');
            }
        })
        .then(function(data) {
            // Xử lý dữ liệu phản hồi
            if (data === 2 && codeCategory !== '') {
                // Nếu danh mục đã tồn tại và codeCategory khác rỗng, hiển thị cảnh báo
                Swal.fire("Category already exists", "", "warning");
            } else {
                // Nếu danh mục chưa tồn tại hoặc là trường hợp update với codeCategory rỗng, hiển thị tùy chọn lưu
                Swal.fire({
                    title: "Do you want to save the changes?",
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Save",
                    denyButtonText: `Don't save`
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Tiến hành gửi form đi
                        document.getElementById('updateCategoryForm').submit();
                    } else if (result.isDenied) {
                        Swal.fire("Changes are not saved", "", "info");
                    }
                });
            }
        })
        .catch(function(error) {
            console.error('Error checking category existence:', error);
            // Xử lý lỗi theo nhu cầu
            Swal.fire("Error", "Failed to check category existence", "error");
        });

    return false; // Ngăn chặn gửi form mặc định
}

// Function to confirm category deletion
function removeCategory(event) {
    event.preventDefault(); // Prevent the default anchor click behavior

    Swal.fire({
        title: "Are you sure you want to delete this category?",
        showCancelButton: true,
        confirmButtonText: "Delete",
        cancelButtonText: "Cancel",
        icon: "warning"
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = event.target.closest('a').href;
        }
    });

    return false; // Prevent the default action
}