package com.datn.datn_mangostore.bean;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "shopping_cart_detail")
public class ShoppingCartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "idShoppingCart")
    private ShoppingCart shoppingCart;
    @ManyToOne
    @JoinColumn(name = "idProductDetail")
    private ProductDetail productDetail;
    private Integer quantity;
    private Integer price;
    private Integer capitalSum;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime dateCreate;
    private Integer status;

    public ShoppingCartDetail(Long id,
                              ShoppingCart shoppingCart,
                              ProductDetail productDetail,
                              Integer quantity,
                              Integer price,
                              Integer capitalSum,
                              LocalDateTime dateCreate,
                              Integer status) {
        this.id = id;
        this.shoppingCart = shoppingCart;
        this.productDetail = productDetail;
        this.quantity = quantity;
        this.price = price;
        this.capitalSum = capitalSum;
        this.dateCreate = dateCreate;
        this.status = status;
    }

    public ShoppingCartDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCapitalSum() {
        return capitalSum;
    }

    public void setCapitalSum(Integer capitalSum) {
        this.capitalSum = capitalSum;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
