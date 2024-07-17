package com.datn.datn_mangostore.request;

public class AddToFavouriteRequest {
    private Long idProduct;
    private Long idSize;
    private Long idColor;

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public Long getIdSize() {
        return idSize;
    }

    public void setIdSize(Long idSize) {
        this.idSize = idSize;
    }

    public Long getIdColor() {
        return idColor;
    }

    public void setIdColor(Long idColor) {
        this.idColor = idColor;
    }
}