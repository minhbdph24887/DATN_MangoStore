package com.datn.datn_mangostore.request;

public class VoucherRequest {
    private String id;
    private String codeVoucher;
    private String nameVoucher;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodeVoucher() {
        return codeVoucher;
    }

    public void setCodeVoucher(String codeVoucher) {
        this.codeVoucher = codeVoucher;
    }

    public String getNameVoucher() {
        return nameVoucher;
    }

    public void setNameVoucher(String nameVoucher) {
        this.nameVoucher = nameVoucher;
    }
}
