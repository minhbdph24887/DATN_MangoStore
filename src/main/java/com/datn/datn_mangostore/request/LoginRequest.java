package com.datn.datn_mangostore.request;

public class LoginRequest {
    private String email;
    private String password;
    private String codeForgot;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodeForgot() {
        return codeForgot;
    }

    public void setCodeForgot(String codeForgot) {
        this.codeForgot = codeForgot;
    }
}
