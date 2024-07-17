package com.datn.datn_mangostore.request;

import java.util.List;

public class UpdateProductRequest {
    private Long id;
    private String nameProduct;
    private List<String> imagesToAdd;
    private List<String> imagesToDelete;
    private String activeImage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public List<String> getImagesToAdd() {
        return imagesToAdd;
    }

    public void setImagesToAdd(List<String> imagesToAdd) {
        this.imagesToAdd = imagesToAdd;
    }

    public List<String> getImagesToDelete() {
        return imagesToDelete;
    }

    public void setImagesToDelete(List<String> imagesToDelete) {
        this.imagesToDelete = imagesToDelete;
    }

    public String getActiveImage() {
        return activeImage;
    }

    public void setActiveImage(String activeImage) {
        this.activeImage = activeImage;
    }
}
