package com.datn.datn_mangostore.bean;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeProduct;
    private String nameProduct;
    @ManyToOne
    @JoinColumn(name = "idImages")
    private Images images;
    private String nameUserCreate;
    private String nameUserUpdate;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime dateCreate;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime dateUpdate;
    private Integer status;
    @OneToMany(mappedBy = "product")
    List<ProductDetail> productDetails;

    public Product(Long id,
                   String codeProduct,
                   String nameProduct,
                   Images images,
                   String nameUserCreate,
                   String nameUserUpdate,
                   LocalDateTime dateCreate,
                   LocalDateTime dateUpdate,
                   Integer status) {
        this.id = id;
        this.codeProduct = codeProduct;
        this.nameProduct = nameProduct;
        this.images = images;
        this.nameUserCreate = nameUserCreate;
        this.nameUserUpdate = nameUserUpdate;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.status = status;
    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeProduct() {
        return codeProduct;
    }

    public void setCodeProduct(String codeProduct) {
        this.codeProduct = codeProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public String getNameUserCreate() {
        return nameUserCreate;
    }

    public void setNameUserCreate(String nameUserCreate) {
        this.nameUserCreate = nameUserCreate;
    }

    public String getNameUserUpdate() {
        return nameUserUpdate;
    }

    public void setNameUserUpdate(String nameUserUpdate) {
        this.nameUserUpdate = nameUserUpdate;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public LocalDateTime getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(LocalDateTime dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
