package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.ProductDetail;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class ProductDetailSpecifications {

    public static Specification<ProductDetail> hasKeyword(String keyword) {
        return (root, query, builder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return null;
            }
            String pattern = "%" + keyword + "%";
            return builder.or(
                    builder.like(root.get("product").get("nameProduct"), pattern),
                    builder.like(root.get("material").get("nameMaterial"), pattern),
                    builder.like(root.get("size").get("nameSize"), pattern),
                    builder.like(root.get("color").get("nameColor"), pattern),
                    builder.like(root.get("origin").get("nameOrigin"), pattern),
                    builder.like(root.get("category").get("nameCategory"), pattern)
            );
        };
    }

    public static Specification<ProductDetail> hasMaterialId(String materialId) {
        return (root, query, builder) -> {
            if (materialId == null || materialId.isEmpty()) {
                return null;
            }
            return builder.equal(root.get("material").get("id"), materialId);
        };
    }

    public static Specification<ProductDetail> hasSizeId(String sizeId) {
        return (root, query, builder) -> {
            if (sizeId == null || sizeId.isEmpty()) {
                return null;
            }
            return builder.equal(root.get("size").get("id"), sizeId);
        };
    }

    public static Specification<ProductDetail> hasColorId(String colorId) {
        return (root, query, builder) -> {
            if (colorId == null || colorId.isEmpty()) {
                return null;
            }
            return builder.equal(root.get("color").get("id"), colorId);
        };
    }

    public static Specification<ProductDetail> hasOriginId(String originId) {
        return (root, query, builder) -> {
            if (originId == null || originId.isEmpty()) {
                return null;
            }
            return builder.equal(root.get("origin").get("id"), originId);
        };
    }

    public static Specification<ProductDetail> hasCategoryId(String categoryId) {
        return (root, query, builder) -> {
            if (categoryId == null || categoryId.isEmpty()) {
                return null;
            }
            return builder.equal(root.get("category").get("id"), categoryId);
        };
    }

    public static Specification<ProductDetail> isActive() {
        return (root, query, builder) -> builder.equal(root.get("status"), 1);
    }

    public static Specification<ProductDetail> sortByPrice(String sortBy) {
        return (root, query, builder) -> {
            if (sortBy == null || sortBy.isEmpty()) {
                return null;
            }

            Join<Object, Object> productJoin = root.join("product");

            switch (sortBy) {
                case "asc":
                    query.orderBy(builder.asc(productJoin.get("price")));
                    break;
                case "desc":
                    query.orderBy(builder.desc(productJoin.get("price")));
                    break;
                // Các trường hợp sắp xếp khác có thể được xử lý tương tự ở đây
                default:
                    break;
            }

            return query.getRestriction();
        };
    }
}