package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.reponse.InvoiceResponse;
import com.datn.datn_mangostore.request.IdInvoiceRequest;
import com.datn.datn_mangostore.request.IdOrderRequest;
import com.datn.datn_mangostore.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "check-confirm-invoice")
    public ResponseEntity<String> checkConfirmInvoiceForAdmin(@RequestBody IdOrderRequest request,
                                                              HttpSession session) {
        return orderService.checkConfirmInvoice(request, session);
    }
}
