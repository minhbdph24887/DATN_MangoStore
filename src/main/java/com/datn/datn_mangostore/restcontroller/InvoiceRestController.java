package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.reponse.InvoiceResponse;
import com.datn.datn_mangostore.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/mangostore/")
public class InvoiceRestController {
    private final CartService cartService;

    public InvoiceRestController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<InvoiceResponse> detailInvoiceById(@PathVariable(name = "id") Long id) {
        return cartService.detailInvoiceById(id);
    }
}
