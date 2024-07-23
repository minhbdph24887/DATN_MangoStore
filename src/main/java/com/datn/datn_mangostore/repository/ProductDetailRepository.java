package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long>, JpaSpecificationExecutor<ProductDetail> {
    @Query(value = "select min(pd.price) as priceMin, max(pd.price) as priceMax from product_detail pd where pd.id_product = :idProduct", nativeQuery = true)
    List<Object[]> findAllPriceByIdProduct(@Param("idProduct") Long idProduct);

    @Query(value = "select count(*) from product_detail where id_size = :idSize", nativeQuery = true)
    Integer countProductDetailBySize(@Param("idSize") Long idSize);

    @Query(value = "select count(*) from product_detail where id_color = :idColor", nativeQuery = true)
    Integer countProductDetailByColor(@Param("idColor") Long idColor);

    @Query(value = "select pd.id, pd.id_product, pd.id_material, pd.id_size, pd.id_color, pd.id_origin, pd.id_category, pd.describe, pd.quantity,\n" +
            "pd.import_price, pd.price, pd.name_user_create, pd.name_user_update, pd.date_create, pd.date_update, pd.product_status, pd.status \n" +
            "from product_detail pd inner join (select id_product, max(id) as MaxId from product_detail group by id_product)\n" +
            "temp on pd.id_product = temp.id_product and pd.id = temp.MaxId inner join product p on pd.id_product = p.id order by pd.price asc", nativeQuery = true)
    Page<ProductDetail> sortProductDetailLowToHigh(Pageable pageable);

    @Query(value = "select pd.id, pd.id_product, pd.id_material, pd.id_size, pd.id_color, pd.id_origin, pd.id_category, pd.describe, pd.quantity,\n" +
            "pd.import_price, pd.price, pd.name_user_create, pd.name_user_update, pd.date_create, pd.date_update, pd.product_status, pd.status \n" +
            "from product_detail pd inner join (select id_product, max(id) as MaxId from product_detail group by id_product)\n" +
            "temp on pd.id_product = temp.id_product and pd.id = temp.MaxId inner join product p on pd.id_product = p.id order by pd.price desc", nativeQuery = true)
    Page<ProductDetail> sortProductDetailHighToLow(Pageable pageable);

    @Query(value = "select pd.id, pd.id_product, pd.id_material, pd.id_size, pd.id_color, pd.id_origin, pd.id_category, pd.describe, pd.quantity,\n" +
            "pd.import_price, pd.price, pd.name_user_create, pd.name_user_update, pd.date_create, pd.date_update, pd.product_status, pd.status\n" +
            "from product_detail pd inner join (select id_product, max(id) as MaxId from product_detail group by id_product) \n" +
            "temp on pd.id_product = temp.id_product and pd.id = temp.MaxId inner join product p on pd.id_product = p.id", nativeQuery = true)
    Page<ProductDetail> getAllProductDetailByIdProduct(Pageable pageable);

    @Query(value = "select pd.id, pd.id_product, pd.id_material, pd.id_size, pd.id_color, pd.id_origin, pd.id_category, pd.describe, pd.quantity, pd.import_price, pd.price, pd.name_user_create, pd.name_user_update, pd.date_create, pd.date_update, pd.product_status, pd.status from product_detail pd inner join (select id_product, max(id) as MaxId from product_detail group by id_product)\n" +
            "temp on pd.id_product = temp.id_product and pd.id = temp.MaxId inner join product p on pd.id_product = p.id\n" +
            "where pd.id_category= :idCategory", nativeQuery = true)
    List<ProductDetail> findAllByIdCategory(@Param("idCategory") Long idCategory);

    @Query(value = "select * from product_detail where id_product= :idProduct and id_size= :idSize and id_color= :idColor", nativeQuery = true)
    ProductDetail getQuantityProductDetail(@Param("idProduct") Long idProduct,
                                           @Param("idSize") Long idSize,
                                           @Param("idColor") Long idColor);

    @Query(value = "select * from product_detail where status= 1 order by id desc", nativeQuery = true)
    List<ProductDetail> getAllProductDetailByStatus1();

    @Query(value = "SELECT * FROM product_detail WHERE status = 0 ORDER BY id DESC", nativeQuery = true)
    List<ProductDetail> getAllProductDetailByStatus0();

    @Query("SELECT pd FROM ProductDetail pd WHERE pd.product.id = :productId AND pd.material.id = :materialId AND pd.size.id = :sizeId AND pd.color.id = :colorId AND pd.origin.id = :originId AND pd.category.id = :categoryId")
    ProductDetail findExistingProductDetail(@Param("productId") Long productId,
                                            @Param("materialId") Long materialId,
                                            @Param("sizeId") Long sizeId, @Param("colorId") Long colorId,
                                            @Param("originId") Long originId, @Param("categoryId") Long categoryId);

    @Query(value = "select * from product_detail where quantity < 50 and quantity > 0", nativeQuery = true)
    List<ProductDetail> findAllProductByQuantity();
}
