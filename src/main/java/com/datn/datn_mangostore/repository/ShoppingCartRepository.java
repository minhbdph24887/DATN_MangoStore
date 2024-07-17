package com.datn.datn_mangostore.repository;

import com.datn.datn_mangostore.bean.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query(value = "select * from shopping_cart where id_account= :idAccount", nativeQuery = true)
    ShoppingCart findByIdAccount(@Param("idAccount") Long id);

    @Query(value = "select * from shopping_cart where id= :idShoppingCart", nativeQuery = true)
    List<ShoppingCart> getAllShoppingCartById(@Param("idShoppingCart") Long idShoppingCart);
}
