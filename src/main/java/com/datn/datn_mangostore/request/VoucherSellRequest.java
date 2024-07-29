package com.datn.datn_mangostore.request;

public class VoucherSellRequest {
    private String idInvoice;
    private String idVoucherCombobox;
    private String idCustomer;
    private String codeVoucher;
    private String totalPayment;

    public String getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(String idInvoice) {
        this.idInvoice = idInvoice;
    }

    public String getIdVoucherCombobox() {
        return idVoucherCombobox;
    }

    public void setIdVoucherCombobox(String idVoucherCombobox) {
        this.idVoucherCombobox = idVoucherCombobox;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getCodeVoucher() {
        return codeVoucher;
    }

    public void setCodeVoucher(String codeVoucher) {
        this.codeVoucher = codeVoucher;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }
}
