package com.datn.datn_mangostore.request;

public class DataRequest {
    private String idUpdate;
    private String idDelete;
    private String numberPhone;
    private String email;

    public String getIdUpdate() {
        return idUpdate;
    }

    public void setIdUpdate(String idUpdate) {
        this.idUpdate = idUpdate;
    }
    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdDelete() {
        return idDelete;
    }

    public void setIdDelete(String idDelete) {
        this.idDelete = idDelete;
    }
}
