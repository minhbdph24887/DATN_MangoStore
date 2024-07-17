package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Product;
import com.datn.datn_mangostore.bean.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  ProductDetailRepository extends JpaRepository<ProductDetail, Long>, JpaSpecificationExecutor<ProductDetail> {
    @Query(value = "select min(pd.price) as priceMin, max(pd.price) as priceMax from product_detail pd where pd.id_product = :idProduct", nativeQuery = true)
    List<Object[]> findAllPriceByIdProduct(@Param("idProduct") Long idProduct);

    @Query(value = "select count(*) from product_detail where id_size = :idSize", nativeQuery = true)
    Integer countProductDetailBySize(@Param("idSize") Long idSize);

    @Query(value = "select count(*) from product_detail where id_color = :idColor", nativeQuery = true)
    Integer countProductDetailByColor(@Param("idColor") Long idColor);

    @Query(value = "select * from product_detail where status= 1 order by id desc", nativeQuery = true)
    List<ProductDetail> getAllProductDetailByStatus1();

    @Query(value = "SELECT * FROM product_detail WHERE status = 0 ORDER BY id DESC", nativeQuery = true)
    List<ProductDetail> getAllProductDetailByStatus0();

//Vinh

    @Query("SELECT pd FROM ProductDetail pd WHERE pd.product.id = :productId AND pd.material.id = :materialId AND pd.size.id = :sizeId AND pd.color.id = :colorId AND pd.origin.id = :originId AND pd.category.id = :categoryId")
    ProductDetail findExistingProductDetail(@Param("productId") Long productId, @Param("materialId") Long materialId, @Param("sizeId") Long sizeId, @Param("colorId") Long colorId, @Param("originId") Long originId, @Param("categoryId") Long categoryId);


}
