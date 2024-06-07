package com.datn.datn_mangostore.bean;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeVoucher;
    private String nameVoucher;
    private Integer quantity;
    private Integer reducedValue;
    private Integer minimumPrice;
    private String voucherFrom;
    @ManyToOne
    @JoinColumn(name = "idRank")
    private Rank rank;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDay;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String userCreate;
    private String userUpdate;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime dateCreate;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime dateUpdate;
    private Integer voucherStatus;
    private String validity;
    private Integer status;

    public Voucher(Long id,
                   String codeVoucher,
                   String nameVoucher,
                   Integer quantity,
                   Integer reducedValue,
                   Integer minimumPrice,
                   String voucherFrom,
                   Rank rank,
                   LocalDate startDay,
                   LocalDate endDate,
                   String userCreate,
                   String userUpdate,
                   LocalDateTime dateCreate,
                   LocalDateTime dateUpdate,
                   Integer voucherStatus,
                   String validity,
                   Integer status) {
        this.id = id;
        this.codeVoucher = codeVoucher;
        this.nameVoucher = nameVoucher;
        this.quantity = quantity;
        this.reducedValue = reducedValue;
        this.minimumPrice = minimumPrice;
        this.voucherFrom = voucherFrom;
        this.rank = rank;
        this.startDay = startDay;
        this.endDate = endDate;
        this.userCreate = userCreate;
        this.userUpdate = userUpdate;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.voucherStatus = voucherStatus;
        this.validity = validity;
        this.status = status;
    }

    public Voucher() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeVoucher() {
        return codeVoucher;
    }

    public void setCodeVoucher(String codeVoucher) {
        this.codeVoucher = codeVoucher;
    }

    public String getNameVoucher() {
        return nameVoucher;
    }

    public void setNameVoucher(String nameVoucher) {
        this.nameVoucher = nameVoucher;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getReducedValue() {
        return reducedValue;
    }

    public void setReducedValue(Integer reducedValue) {
        this.reducedValue = reducedValue;
    }

    public Integer getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(Integer minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public String getVoucherFrom() {
        return voucherFrom;
    }

    public void setVoucherFrom(String voucherFrom) {
        this.voucherFrom = voucherFrom;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public LocalDate getStartDay() {
        return startDay;
    }

    public void setStartDay(LocalDate startDay) {
        this.startDay = startDay;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    public Integer getVoucherStatus() {
        return voucherStatus;
    }

    public void setVoucherStatus(Integer voucherStatus) {
        this.voucherStatus = voucherStatus;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
