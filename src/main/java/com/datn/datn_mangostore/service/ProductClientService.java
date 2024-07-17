package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.request.AddToCartRequest;
import com.datn.datn_mangostore.request.AddToFavouriteRequest;
import com.datn.datn_mangostore.request.QuantityRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface ProductClientService {
    String indexProductClient(Model model,
                              HttpSession session,
                              String sortDirection,
                              Integer pageNo);

    String detailProductClient(Long idProductDetail,
                               Model model,
                               HttpSession session);

    ResponseEntity<?> quantityProductDetail(QuantityRequest request);

    boolean addToCart(AddToCartRequest request,
                      HttpSession session);

    boolean addToFavourite(AddToFavouriteRequest request,
                           HttpSession session);
}
