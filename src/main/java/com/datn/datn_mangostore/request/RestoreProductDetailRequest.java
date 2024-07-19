package com.datn.datn_mangostore.request;

public class RestoreProductDetailRequest {
    private Long idProductDetail;
    private Integer quantity;

    public Long getIdProductDetail() {
        return idProductDetail;
    }

    public void setIdProductDetail(Long idProductDetail) {
        this.idProductDetail = idProductDetail;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
