package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.request.AddToCartRequest;
import com.datn.datn_mangostore.request.AddToFavouriteRequest;
import com.datn.datn_mangostore.request.QuantityRequest;
import com.datn.datn_mangostore.service.ProductClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/mangostore/")
public class ProductDetailClientRestController {
    private final ProductClientService productClientService;

    public ProductDetailClientRestController(ProductClientService productClientService) {
        this.productClientService = productClientService;
    }

    @PostMapping(value = "find-product-by-variant")
    public ResponseEntity<?> findProductByVariant(@RequestBody QuantityRequest request) {
        return productClientService.quantityProductDetail(request);
    }

    @PostMapping(value = "add-to-cart/client")
    public ResponseEntity<String> addToCart(@RequestBody AddToCartRequest request,
                                            HttpSession session) {
        return productClientService.addToCart(request,
                session);
    }

    @PostMapping(value = "add-to-favourite/client")
    public boolean addToFavourite(@RequestBody AddToFavouriteRequest request,
                                  HttpSession session) {
        return productClientService.addToFavourite(request,
                session);
    }
}
