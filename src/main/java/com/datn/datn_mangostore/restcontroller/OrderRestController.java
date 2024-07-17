package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.reponse.InvoiceResponse;
import com.datn.datn_mangostore.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/mangostore/admin/")
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "invoice/{id}")
    public ResponseEntity<InvoiceResponse> detailsInvoiceByIdForAdmin(@PathVariable(name = "id") Long id) {
        return orderService.detailsInvoiceByIdForAdmin(id);
    }
}
