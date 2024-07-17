package com.datn.datn_mangostore.request;

public class QuantityCartRequest {
    private String idProductDetail;
    private String quantityNew;

    public String getIdProductDetail() {
        return idProductDetail;
    }

    public void setIdProductDetail(String idProductDetail) {
        this.idProductDetail = idProductDetail;
    }

    public String getQuantityNew() {
        return quantityNew;
    }

    public void setQuantityNew(String quantityNew) {
        this.quantityNew = quantityNew;
    }
}
