package com.datn.datn_mangostore.request;

public class InvoiceRequest {
    private Long idInvoice;
    private Integer returnClientMoney;

    public Long getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(Long idInvoice) {
        this.idInvoice = idInvoice;
    }

    public Integer getReturnClientMoney() {
        return returnClientMoney;
    }

    public void setReturnClientMoney(Integer returnClientMoney) {
        this.returnClientMoney = returnClientMoney;
    }
}
