package com.datn.datn_mangostore.bean;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String note;
    private Integer status;

    @OneToMany(mappedBy = "role")
    List<Authentication> authentications;

    public Role(Long id,
                String name,
                String note,
                Integer status,
                List<Authentication> authentications) {
        this.id = id;
        this.name = name;
        this.note = note;
        this.status = status;
        this.authentications = authentications;
    }

    public Role() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Authentication> getAuthentications() {
        return authentications;
    }

    public void setAuthentications(List<Authentication> authentications) {
        this.authentications = authentications;
    }
}
