package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.ShoppingCartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartDetailRepository extends JpaRepository<ShoppingCartDetail, Long> {
    @Query(value = "select * from shopping_cart_detail where id_shopping_cart= :shoppingCart and status= 1", nativeQuery = true)
    List<ShoppingCartDetail> getAllShoppingCart(@Param("shoppingCart") Long idDetailShoppingCart);
}
