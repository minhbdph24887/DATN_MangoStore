package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.request.AddProductRequest;
import com.datn.datn_mangostore.request.UpdateProductRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface ProductService {
    String indexProductAdmin(Model model,
                             HttpSession session,
                             String keyword);

    String createProductAdmin(Model model,
                              HttpSession session);

    boolean addProductAPI(AddProductRequest request, HttpSession session);

    boolean updateProductAPI(UpdateProductRequest request, HttpSession session);

    String detailProduct(Model model,
                         HttpSession session,
                         Long idProduct);

    String deleteProduct(Long idProduct);

    void updateProduct(UpdateProductRequest request, HttpSession session);

    String restoreProduct(Long aLong);

    Integer findByProductCreateExit(String name );
    Integer findByProductUpdateExit( String codeProduct ,String name);

}


