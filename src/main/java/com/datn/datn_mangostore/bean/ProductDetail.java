package com.datn.datn_mangostore.bean;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "productDetail")
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "idProduct")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "idMaterial")
    private Material material;
    @ManyToOne
    @JoinColumn(name = "idSize")
    private Size size;
    @ManyToOne
    @JoinColumn(name = "idColor")
    private Color color;
    @ManyToOne
    @JoinColumn(name = "idOrigin")
    private Origin origin;
    @ManyToOne
    @JoinColumn(name = "idCategory")
    private Category category;
    private String describe;
    private Integer quantity;
    private Integer importPrice;
    private Integer price;
    private String nameUserCreate;
    private String nameUserUpdate;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime dateCreate;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime dateUpdate;
    private Integer productStatus;
    private Integer status;

    public ProductDetail(Long id,
                         Product product,
                         Material material,
                         Size size,
                         Color color,
                         Origin origin,
                         Category category,
                         String describe,
                         Integer quantity,
                         Integer importPrice,
                         Integer price,
                         String nameUserCreate,
                         String nameUserUpdate,
                         LocalDateTime dateCreate,
                         LocalDateTime dateUpdate,
                         Integer productStatus,
                         Integer status) {
        this.id = id;
        this.product = product;
        this.material = material;
        this.size = size;
        this.color = color;
        this.origin = origin;
        this.category = category;
        this.describe = describe;
        this.quantity = quantity;
        this.importPrice = importPrice;
        this.price = price;
        this.nameUserCreate = nameUserCreate;
        this.nameUserUpdate = nameUserUpdate;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.productStatus = productStatus;
        this.status = status;
    }

    public ProductDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(Integer importPrice) {
        this.importPrice = importPrice;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    public Integer getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Integer productStatus) {
        this.productStatus = productStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
