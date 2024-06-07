package com.datn.datn_mangostore.bean;

import jakarta.persistence.*;

@Entity
@Table(name = "shopping_cart")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeShoppingCart;
    @ManyToOne
    @JoinColumn(name = "idAccount")
    private Account account;
    private Integer totalShoppingCart;
    private Integer status;

    public ShoppingCart(Long id,
                        String codeShoppingCart,
                        Account account,
                        Integer totalShoppingCart,
                        Integer status) {
        this.id = id;
        this.codeShoppingCart = codeShoppingCart;
        this.account = account;
        this.totalShoppingCart = totalShoppingCart;
        this.status = status;
    }

    public ShoppingCart() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeShoppingCart() {
        return codeShoppingCart;
    }

    public void setCodeShoppingCart(String codeShoppingCart) {
        this.codeShoppingCart = codeShoppingCart;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Integer getTotalShoppingCart() {
        return totalShoppingCart;
    }

    public void setTotalShoppingCart(Integer totalShoppingCart) {
        this.totalShoppingCart = totalShoppingCart;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
