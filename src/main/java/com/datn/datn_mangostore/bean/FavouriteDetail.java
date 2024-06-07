package com.datn.datn_mangostore.bean;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "favourite_detail")
public class FavouriteDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "idFavourite")
    private Favourite favourite;
    @ManyToOne
    @JoinColumn(name = "idProductDetail")
    private ProductDetail productDetail;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime dateCreate;
    private Integer status;

    public FavouriteDetail(Long id,
                           Favourite favourite,
                           ProductDetail productDetail,
                           LocalDateTime dateCreate,
                           Integer status) {
        this.id = id;
        this.favourite = favourite;
        this.productDetail = productDetail;
        this.dateCreate = dateCreate;
        this.status = status;
    }

    public FavouriteDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Favourite getFavourite() {
        return favourite;
    }

    public void setFavourite(Favourite favourite) {
        this.favourite = favourite;
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
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
