package com.datn.datn_mangostore.bean;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "origin")
public class Origin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeOrigin;
    private String nameOrigin;
    private String nameUserCreate;
    private String nameUserUpdate;

    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime dateCreate;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime dateUpdate;
    private Integer status;
    @OneToMany(mappedBy = "origin")
    List<ProductDetail> productDetails;

    public Origin(Long id,
                  String codeOrigin,
                  String nameOrigin,
                  String nameUserCreate,
                  String nameUserUpdate,
                  LocalDateTime dateCreate,
                  LocalDateTime dateUpdate,
                  Integer status) {
        this.id = id;
        this.codeOrigin = codeOrigin;
        this.nameOrigin = nameOrigin;
        this.nameUserCreate = nameUserCreate;
        this.nameUserUpdate = nameUserUpdate;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.status = status;
    }

    public Origin() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeOrigin() {
        return codeOrigin;
    }

    public void setCodeOrigin(String codeOrigin) {
        this.codeOrigin = codeOrigin;
    }

    public String getNameOrigin() {
        return nameOrigin;
    }

    public void setNameOrigin(String nameOrigin) {
        this.nameOrigin = nameOrigin;
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
