package com.datn.datn_mangostore.request;

import com.datn.datn_mangostore.bean.ProductDetail;

import java.util.List;

public class ProductDetailRequest {
    private Long name;
    private Long material;
    private String describe;
    private String imagesProductDetail;
    private String importPrice;
    private String price;
    private Integer status;
    private Long origin;
    private Integer quantity;
    private Long idProduct;
    private List<Long> idSizes;
    private List<Long> idColors;
    private List<ProductDetail> productVariants;

    public Long getName() {
        return name;
    }

    public void setName(Long name) {
        this.name = name;
    }

    public Long getMaterial() {
        return material;
    }

    public void setMaterial(Long material) {
        this.material = material;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getImagesProductDetail() {
        return imagesProductDetail;
    }

    public void setImagesProductDetail(String imagesProductDetail) {
        this.imagesProductDetail = imagesProductDetail;
    }

    public String getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(String importPrice) {
        this.importPrice = importPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getOrigin() {
        return origin;
    }

    public void setOrigin(Long origin) {
        this.origin = origin;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public List<Long> getIdSizes() {
        return idSizes;
    }

    public void setIdSizes(List<Long> idSizes) {
        this.idSizes = idSizes;
    }

    public List<Long> getIdColors() {
        return idColors;
    }

    public void setIdColors(List<Long> idColors) {
        this.idColors = idColors;
    }

    public List<ProductDetail> getProductVariants() {
        return productVariants;
    }

    public void setProductVariants(List<ProductDetail> productVariants) {
        this.productVariants = productVariants;
    }
}
