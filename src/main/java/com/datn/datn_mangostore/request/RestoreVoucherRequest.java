package com.datn.datn_mangostore.request;

public class RestoreVoucherRequest {
    private Long idVoucher;
    private Integer quantity;

    public Long getIdVoucher() {
        return idVoucher;
    }

    public void setIdVoucher(Long idVoucher) {
        this.idVoucher = idVoucher;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
