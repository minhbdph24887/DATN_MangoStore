package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.request.QuantityCartRequest;
import com.datn.datn_mangostore.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/mangostore/")
public class CartRestController {
    private final CartService cartService;

    public CartRestController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping(value = "cart/increase")
    public ResponseEntity<String> checkUpdateProfile(@RequestBody QuantityCartRequest request) {
        return cartService.checkIncreaseQuantityForCart(request);
    }
}
