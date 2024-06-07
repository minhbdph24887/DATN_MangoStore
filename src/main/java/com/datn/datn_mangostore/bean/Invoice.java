package com.datn.datn_mangostore.bean;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeInvoice;
    private String nameInvoice;
    @ManyToOne
    @JoinColumn(name = "idAccount")
    private Account account;
    private String invoiceForm;
    private Long idCustomer;
    @ManyToOne
    @JoinColumn(name = "idVoucher")
    private Voucher voucher;
    @ManyToOne
    @JoinColumn(name = "idVoucherClient")
    private VoucherClient voucherClient;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime invoiceCreationDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime invoicePaymentDate;
    private Integer customerPoints;
    private Integer totalInvoiceAmount;
    private String payments;
    private Integer returnClientMoney;
    private Integer shippingMoney;
    private Integer leftoverMoney;
    private Integer totalPayment;
    private Integer invoiceStatus;

    public Invoice(Long id,
                   String codeInvoice,
                   String nameInvoice,
                   Account account,
                   String invoiceForm,
                   Long idCustomer,
                   Voucher voucher,
                   VoucherClient voucherClient,
                   LocalDateTime invoiceCreationDate,
                   LocalDateTime invoicePaymentDate,
                   Integer customerPoints,
                   Integer totalInvoiceAmount,
                   String payments,
                   Integer returnClientMoney,
                   Integer shippingMoney,
                   Integer leftoverMoney,
                   Integer totalPayment,
                   Integer invoiceStatus) {
        this.id = id;
        this.codeInvoice = codeInvoice;
        this.nameInvoice = nameInvoice;
        this.account = account;
        this.invoiceForm = invoiceForm;
        this.idCustomer = idCustomer;
        this.voucher = voucher;
        this.voucherClient = voucherClient;
        this.invoiceCreationDate = invoiceCreationDate;
        this.invoicePaymentDate = invoicePaymentDate;
        this.customerPoints = customerPoints;
        this.totalInvoiceAmount = totalInvoiceAmount;
        this.payments = payments;
        this.returnClientMoney = returnClientMoney;
        this.shippingMoney = shippingMoney;
        this.leftoverMoney = leftoverMoney;
        this.totalPayment = totalPayment;
        this.invoiceStatus = invoiceStatus;
    }

    public Invoice() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeInvoice() {
        return codeInvoice;
    }

    public void setCodeInvoice(String codeInvoice) {
        this.codeInvoice = codeInvoice;
    }

    public String getNameInvoice() {
        return nameInvoice;
    }

    public void setNameInvoice(String nameInvoice) {
        this.nameInvoice = nameInvoice;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getInvoiceForm() {
        return invoiceForm;
    }

    public void setInvoiceForm(String invoiceForm) {
        this.invoiceForm = invoiceForm;
    }

    public Long getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Long idCustomer) {
        this.idCustomer = idCustomer;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public VoucherClient getVoucherClient() {
        return voucherClient;
    }

    public void setVoucherClient(VoucherClient voucherClient) {
        this.voucherClient = voucherClient;
    }

    public LocalDateTime getInvoiceCreationDate() {
        return invoiceCreationDate;
    }

    public void setInvoiceCreationDate(LocalDateTime invoiceCreationDate) {
        this.invoiceCreationDate = invoiceCreationDate;
    }

    public LocalDateTime getInvoicePaymentDate() {
        return invoicePaymentDate;
    }

    public void setInvoicePaymentDate(LocalDateTime invoicePaymentDate) {
        this.invoicePaymentDate = invoicePaymentDate;
    }

    public Integer getCustomerPoints() {
        return customerPoints;
    }

    public void setCustomerPoints(Integer customerPoints) {
        this.customerPoints = customerPoints;
    }

    public Integer getTotalInvoiceAmount() {
        return totalInvoiceAmount;
    }

    public void setTotalInvoiceAmount(Integer totalInvoiceAmount) {
        this.totalInvoiceAmount = totalInvoiceAmount;
    }

    public Integer getReturnClientMoney() {
        return returnClientMoney;
    }

    public void setReturnClientMoney(Integer returnClientMoney) {
        this.returnClientMoney = returnClientMoney;
    }

    public Integer getShippingMoney() {
        return shippingMoney;
    }

    public void setShippingMoney(Integer shippingMoney) {
        this.shippingMoney = shippingMoney;
    }

    public Integer getLeftoverMoney() {
        return leftoverMoney;
    }

    public void setLeftoverMoney(Integer leftoverMoney) {
        this.leftoverMoney = leftoverMoney;
    }

    public String getPayments() {
        return payments;
    }

    public void setPayments(String payments) {
        this.payments = payments;
    }

    public Integer getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(Integer totalPayment) {
        this.totalPayment = totalPayment;
    }

    public Integer getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }
}

