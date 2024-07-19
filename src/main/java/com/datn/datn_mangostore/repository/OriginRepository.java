package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Origin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OriginRepository extends JpaRepository<Origin, Long> {
    @Query(value = "select * from origin where status= 1 order by id desc", nativeQuery = true)
    List<Origin> getAllOriginByStatus1();

    @Query(value = "select * from origin where status= 0 order by id desc", nativeQuery = true)
    List<Origin> getAllOriginByStatus0();


    @Query(value = "select * from origin where name_origin COLLATE Latin1_General_CI_AI like %:searchOrigin% and status= 1", nativeQuery = true)
    List<Origin> searchOrigin(@Param("searchOrigin") String searchOrigin);

    @Query(value = "select * from origin where name_origin = :nameOrigin", nativeQuery = true)
    Origin findByNameExit(@Param("nameOrigin") String nameOrigin);

    @Query(value = "select * from origin where code_origin = :codeOrigin", nativeQuery = true)
    Origin findByCodeExit(@Param("codeOrigin") String codeOrigin);

}
