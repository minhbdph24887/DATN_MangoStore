package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    @Query(value = "select * from color where status= 1 order by id desc", nativeQuery = true)
    List<Color> getAllColorByStatus1();

    @Query(value = "select * from color where status= 0 order by id desc", nativeQuery = true)
    List<Color> getAllColorByStatus0();

    @Query(value = "SELECT * FROM color WHERE name_color LIKE %:searchColor% AND status = 1", nativeQuery = true)
    List<Color> searchColor(@Param("searchColor") String searchColor);


    @Query(value = "select * from color where name_color= :nameColor and status= 1", nativeQuery = true)
    Color findByName(@Param("nameColor") String nameColor);

    //    vinh3
    @Query(value = "select * from color where name_color= :nameColor ", nativeQuery = true)
    Color findByNameExit(@Param("nameColor") String nameColor);

    @Query(value = "select * from color where code_color= :codeColor ", nativeQuery = true)
    Color findByCodeExit(@Param("codeColor") String codeColor);

}
