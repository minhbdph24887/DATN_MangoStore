package com.datn.datn_mangostore.request;

import java.util.List;

public class AddProductRequest {
    private String nameProduct;
    private List<String> listImages;
    private String imagesActive;

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getImagesActive() {
        return imagesActive;
    }

    public void setImagesActive(String imagesActive) {
        this.imagesActive = imagesActive;
    }

    public List<String> getListImages() {
        return listImages;
    }

    public void setListImages(List<String> listImages) {
        this.listImages = listImages;
    }
}
