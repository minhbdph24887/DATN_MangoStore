package com.datn.datn_mangostore.bean;

import jakarta.persistence.*;

@Entity
@Table(name = "favourite")
public class Favourite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeFavourite;
    @ManyToOne
    @JoinColumn(name = "idAccount")
    private Account account;
    private Integer status;

    public Favourite(Long id,
                     String codeFavourite,
                     Account account,
                     Integer status) {
        this.id = id;
        this.codeFavourite = codeFavourite;
        this.account = account;
        this.status = status;
    }

    public Favourite() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeFavourite() {
        return codeFavourite;
    }

    public void setCodeFavourite(String codeFavourite) {
        this.codeFavourite = codeFavourite;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
