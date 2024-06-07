package com.datn.datn_mangostore.bean;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "images")
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeImages;
    private String imagesFile;
    private Long saveToProduct;
    private String nameUserCreate;
    private String nameUserUpdate;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime dateCreate;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime dateUpdate;
    private Integer status;

    public Images(Long id,
                  String codeImages,
                  String imagesFile,
                  Long saveToProduct,
                  String nameUserCreate,
                  String nameUserUpdate,
                  LocalDateTime dateCreate,
                  LocalDateTime dateUpdate,
                  Integer status) {
        this.id = id;
        this.codeImages = codeImages;
        this.imagesFile = imagesFile;
        this.saveToProduct = saveToProduct;
        this.nameUserCreate = nameUserCreate;
        this.nameUserUpdate = nameUserUpdate;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.status = status;
    }

    public Images() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeImages() {
        return codeImages;
    }

    public void setCodeImages(String codeImages) {
        this.codeImages = codeImages;
    }

    public String getImagesFile() {
        return imagesFile;
    }

    public void setImagesFile(String imagesFile) {
        this.imagesFile = imagesFile;
    }

    public Long getSaveToProduct() {
        return saveToProduct;
    }

    public void setSaveToProduct(Long saveToProduct) {
        this.saveToProduct = saveToProduct;
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
