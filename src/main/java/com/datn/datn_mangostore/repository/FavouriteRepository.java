package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
}
