package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Long> {
    @Query(value = "select * from images where images_file = :fileImages", nativeQuery = true)
    Images findByFileImages(@Param("fileImages") String fileImages);

    List<Images> findBySaveToProduct(Long productId);

    @Query("SELECT i FROM Images i WHERE i.saveToProduct = :productId")
    List<Images> findByProductId(@Param("productId") Long productId);
//Vinh

    @Query("SELECT i.imagesFile FROM Images i")
    List<String> findAllImagesFiles();

}
