package com.datn.datn_mangostore.bean;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "voucherClient")
public class VoucherClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeVoucher;
    private String nameVoucher;
    private Integer reducedValue;
    private Integer minimumPrice;
    @ManyToOne
    @JoinColumn(name = "idAccount")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "idVoucher")
    private Voucher voucher;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime saveDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDay;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Integer voucherStatus;
    private String validity;
    private Integer status;

    public VoucherClient(Long id,
                         String codeVoucher,
                         String nameVoucher,
                         Integer reducedValue,
                         Integer minimumPrice,
                         Account account,
                         Voucher voucher,
                         LocalDateTime saveDate,
                         LocalDate startDay,
                         LocalDate endDate,
                         Integer voucherStatus,
                         String validity,
                         Integer status) {
        this.id = id;
        this.codeVoucher = codeVoucher;
        this.nameVoucher = nameVoucher;
        this.reducedValue = reducedValue;
        this.minimumPrice = minimumPrice;
        this.account = account;
        this.voucher = voucher;
        this.saveDate = saveDate;
        this.startDay = startDay;
        this.endDate = endDate;
        this.voucherStatus = voucherStatus;
        this.validity = validity;
        this.status = status;
    }

    public VoucherClient() {
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public LocalDateTime getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(LocalDateTime saveDate) {
        this.saveDate = saveDate;
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
