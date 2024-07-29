package com.datn.datn_mangostore.request;

public class IdAuthenticationRequest {
    private String idAccount;
    private String idRole;
    private String idAuthentication;

    public String getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(String idAccount) {
        this.idAccount = idAccount;
    }

    public String getIdRole() {
        return idRole;
    }

    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public String getIdAuthentication() {
        return idAuthentication;
    }

    public void setIdAuthentication(String idAuthentication) {
        this.idAuthentication = idAuthentication;
    }
}
