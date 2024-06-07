package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    @Query(value = "select min(pd.price) as priceMin, max(pd.price) as priceMax from product_detail pd where pd.id_product = :idProduct", nativeQuery = true)
    List<Object[]> findAllPriceByIdProduct(@Param("idProduct") Long idProduct);

    @Query(value = "select count(*) from product_detail where id_size = :idSize", nativeQuery = true)
    Integer countProductDetailBySize(@Param("idSize") Long idSize);

    @Query(value = "select count(*) from product_detail where id_color = :idColor", nativeQuery = true)
    Integer countProductDetailByColor(@Param("idColor") Long idColor);
}
