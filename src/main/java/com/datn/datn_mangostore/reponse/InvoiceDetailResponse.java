package com.datn.datn_mangostore.reponse;

import com.datn.datn_mangostore.bean.InvoiceDetail;

import java.text.DecimalFormat;

public class InvoiceDetailResponse {
    private Long id;
    private String imageProduct;
    private String productName;
    private String nameSize;
    private String nameColor;
    private Integer quantity;
    private String price;

    public InvoiceDetailResponse(InvoiceDetail invoiceDetail) {
        this.id = invoiceDetail.getId();
        this.imageProduct = invoiceDetail.getProductDetail().getProduct().getImages().getImagesFile();
        this.productName = invoiceDetail.getProductDetail().getProduct().getNameProduct();
        this.nameSize = invoiceDetail.getProductDetail().getSize().getNameSize();
        this.nameColor = invoiceDetail.getProductDetail().getColor().getNameColor();
        this.quantity = invoiceDetail.getQuantity();

        DecimalFormat formatterInput = new DecimalFormat("###,###,###");
        this.price = formatterInput.format(invoiceDetail.getPrice()) + " VND";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageProduct() {
        return imageProduct;
    }

    public void setImageProduct(String imageProduct) {
        this.imageProduct = imageProduct;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getNameSize() {
        return nameSize;
    }

    public void setNameSize(String nameSize) {
        this.nameSize = nameSize;
    }

    public String getNameColor() {
        return nameColor;
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
