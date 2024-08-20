package com.datn.datn_mangostore.reponse;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.AddressClient;
import com.datn.datn_mangostore.bean.Invoice;
import com.datn.datn_mangostore.bean.InvoiceDetail;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceResponse {
    private Long id;
    private String codeInvoice;
    private String nameInvoice;
    private String invoicePaymentDate;
    private String totalPayment;
    private Integer invoiceStatus;
    private String customerAddress;
    private String nameCustomer;
    private List<InvoiceDetailResponse> invoiceDetails;

    public InvoiceResponse(Invoice invoice,
                           Account account,
                           List<InvoiceDetail> invoiceDetails) {
        this.id = invoice.getId();
        this.codeInvoice = invoice.getCodeInvoice();
        this.nameInvoice = invoice.getNameInvoice();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
        this.invoicePaymentDate = invoice.getInvoicePaymentDate().format(formatter);

        DecimalFormat formatterInput = new DecimalFormat("###,###,###");
        this.totalPayment = formatterInput.format(invoice.getTotalPayment()) + " VND";

        this.invoiceStatus = invoice.getInvoiceStatus();

        AddressClient addressClient = account.getAddressClient();
        if (addressClient != null) {
            this.customerAddress = invoice.getDeliveryAddress();
        } else {
            this.customerAddress = "Address not available";
        }

        this.nameCustomer = account.getFullName();

        this.invoiceDetails = invoiceDetails.stream()
                .map(InvoiceDetailResponse::new)
                .collect(Collectors.toList());
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

    public String getInvoicePaymentDate() {
        return invoicePaymentDate;
    }

    public void setInvoicePaymentDate(String invoicePaymentDate) {
        this.invoicePaymentDate = invoicePaymentDate;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public Integer getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public List<InvoiceDetailResponse> getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(List<InvoiceDetailResponse> invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
    }
}
