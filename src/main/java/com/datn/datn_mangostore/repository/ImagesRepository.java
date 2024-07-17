package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Long> {
    @Query(value = "select * from images where save_to_product= :idProduct", nativeQuery = true)
    List<Images> findAllImagesByProduct(@Param("idProduct") Long idProduct);
}