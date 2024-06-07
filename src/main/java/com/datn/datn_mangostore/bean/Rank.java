package com.datn.datn_mangostore.bean;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "rank")
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeRank;
    private String nameRank;
    private Integer minimumScore;
    private Integer maximumScore;
    private String userCreate;
    private String userUpdate;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime dateCreate;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime dateUpdate;
    private Integer status;

    public Rank(Long id,
                String codeRank,
                String nameRank,
                Integer minimumScore,
                Integer maximumScore,
                String userCreate,
                String userUpdate,
                LocalDateTime dateCreate,
                LocalDateTime dateUpdate,
                Integer status) {
        this.id = id;
        this.codeRank = codeRank;
        this.nameRank = nameRank;
        this.minimumScore = minimumScore;
        this.maximumScore = maximumScore;
        this.userCreate = userCreate;
        this.userUpdate = userUpdate;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.status = status;
    }

    public Rank() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeRank() {
        return codeRank;
    }

    public void setCodeRank(String codeRank) {
        this.codeRank = codeRank;
    }

    public String getNameRank() {
        return nameRank;
    }

    public void setNameRank(String nameRank) {
        this.nameRank = nameRank;
    }

    public Integer getMinimumScore() {
        return minimumScore;
    }

    public void setMinimumScore(Integer minimumScore) {
        this.minimumScore = minimumScore;
    }

    public Integer getMaximumScore() {
        return maximumScore;
    }

    public void setMaximumScore(Integer maximumScore) {
        this.maximumScore = maximumScore;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public String getUserUpdate() {
        return userUpdate;
    }

    public void setUserUpdate(String userUpdate) {
        this.userUpdate = userUpdate;
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
