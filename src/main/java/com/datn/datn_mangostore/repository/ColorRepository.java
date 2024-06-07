package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
}
