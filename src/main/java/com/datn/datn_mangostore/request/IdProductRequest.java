package com.datn.datn_mangostore.request;

public class IdProductRequest {
    private Long idInvoice;
    private Long idProductDetail;
    private Integer newQuantity;

    public Long getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(Long idInvoice) {
        this.idInvoice = idInvoice;
    }

    public Long getIdProductDetail() {
        return idProductDetail;
    }

    public void setIdProductDetail(Long idProductDetail) {
        this.idProductDetail = idProductDetail;
    }

    public Integer getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(Integer newQuantity) {
        this.newQuantity = newQuantity;
    }
}
