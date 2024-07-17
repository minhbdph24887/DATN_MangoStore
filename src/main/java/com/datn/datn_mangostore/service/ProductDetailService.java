package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.bean.ProductDetail;
import com.datn.datn_mangostore.request.CreateProductRequest;
import com.datn.datn_mangostore.request.ProductDetailRequest;
import com.datn.datn_mangostore.request.RestoreProductDetailRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProductDetailService {

    String viewCreateProductDetail(Model model,
                                   HttpSession session);

    Map<String, List<String>> saveProductDetailAPI(CreateProductRequest request,
                                                   HttpSession session,
                                                   HttpServletResponse response) throws IOException;

    String addProductDetail(ProductDetailRequest productDetailForm,
                            BindingResult result,
                            HttpSession session,
                            Model model);

    String editProductDetail(Long idProductDetail,
                             Model model,
                             HttpSession session);

    String updateProductDetail(ProductDetail editProductDetail,
                               BindingResult result,
                               HttpSession session);

    String deleteProductDetail(Long idProductDetail);

    String restoreProductDetail(Long aLong) ;

    String indexProductDetail(Model model, HttpSession session, String keyword, String materialId,
                              String sizeId, String colorId, String originId, String categoryId, String sortBy,
                              Pageable pageable);


    boolean restoreProductDetailAPI(RestoreProductDetailRequest request);



}
