package com.datn.datn_mangostore.request;

public class IdVoucherSellRequest {
    private Long idInvoice;
    private Long idVoucher;

    public Long getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(Long idInvoice) {
        this.idInvoice = idInvoice;
    }

    public Long getIdVoucher() {
        return idVoucher;
    }

    public void setIdVoucher(Long idVoucher) {
        this.idVoucher = idVoucher;
    }
}
