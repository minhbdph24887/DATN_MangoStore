package com.datn.datn_mangostore.bean;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "material")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeMaterial;
    private String nameMaterial;
    private String nameUserCreate;
    private String nameUserUpdate;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime dateCreate;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime dateUpdate;
    private Integer status;
    @OneToMany(mappedBy = "material")
    List<ProductDetail> productDetails;

    public Material(Long id,
                    String codeMaterial,
                    String nameMaterial,
                    String nameUserCreate,
                    String nameUserUpdate,
                    LocalDateTime dateCreate,
                    LocalDateTime dateUpdate,
                    Integer status) {
        this.id = id;
        this.codeMaterial = codeMaterial;
        this.nameMaterial = nameMaterial;
        this.nameUserCreate = nameUserCreate;
        this.nameUserUpdate = nameUserUpdate;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.status = status;
    }

    public Material() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeMaterial() {
        return codeMaterial;
    }

    public void setCodeMaterial(String codeMaterial) {
        this.codeMaterial = codeMaterial;
    }

    public String getNameMaterial() {
        return nameMaterial;
    }

    public void setNameMaterial(String nameMaterial) {
        this.nameMaterial = nameMaterial;
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
