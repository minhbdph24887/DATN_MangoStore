package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "select * from category where status= 1 order by id desc", nativeQuery = true)
    List<Category> getAllCategoryByStatus1();

    @Query(value = "select * from category where status= 0 order by id desc", nativeQuery = true)
    List<Category> getAllCategoryByStatus0();

    @Query(value = "select * from category where name_category collate Latin1_General_CI_AI like %:searchCategory% and status= 1", nativeQuery = true)
    List<Category> searchCategory(@Param("searchCategory") String searchCategory);

    @Query(value = "select * from category where name_category = :nameCategory", nativeQuery = true)
    Category findByNameExit(@Param("nameCategory") String nameCategory);
}
