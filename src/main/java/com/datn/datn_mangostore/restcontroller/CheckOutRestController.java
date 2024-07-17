package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.request.CodeVoucherRequest;
import com.datn.datn_mangostore.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/mangostore/")
public class CheckOutRestController {
    private final CartService cartService;

    public CheckOutRestController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping(value = "voucher-invoice/add")
    public ResponseEntity<String> addVoucherByCodeVoucher(@RequestBody CodeVoucherRequest request,
                                                          HttpSession session) {
        return cartService.addVoucherByCodeVoucher(request, session);
    }

    @GetMapping(value = "cart/update-status")
    public ResponseEntity<String> updateInvoiceStatusCash(@RequestParam("id") Long idInvoice) {
        return cartService.updateCashInvoice(idInvoice);
    }
}
