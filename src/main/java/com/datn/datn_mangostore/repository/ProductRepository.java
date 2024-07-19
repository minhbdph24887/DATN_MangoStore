package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select * from product where status= 1 order by id desc", nativeQuery = true)
    List<Product> getAllProductByStatus1();

    @Query(value = "select * from product where status= 0 order by id desc", nativeQuery = true)
    List<Product> getAllProductByStatus0();

    @Query(value = "select * from product where name_product COLLATE Latin1_General_CI_AI like %:searchProduct% and status= 1", nativeQuery = true)
    List<Product> searchProduct(@Param("searchProduct") String searchProduct);

    @Query(value = "select * from product where id= :idProduct and status= 1", nativeQuery = true)
    Product findByMa(@Param("idProduct") Long idProduct);

    @Query(value = "select * from product where name_product= :nameProduct ", nativeQuery = true)
    Product findByNameExit(@Param("nameProduct") String nameProduct);


}

