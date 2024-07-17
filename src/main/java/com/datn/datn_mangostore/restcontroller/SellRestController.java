package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.request.*;
import com.datn.datn_mangostore.service.SellService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/mangostore/admin/")
public class SellRestController {
    private final SellService sellService;

    public SellRestController(SellService sellService) {
        this.sellService = sellService;
    }

    @PostMapping(value = "sell/create")
    public ResponseEntity<String> createInvoiceAPI(HttpSession session) {
        return sellService.createInvoiceAPI(session);
    }

    @PostMapping(value = "sell/add-client")
    public ResponseEntity<String> addNumberClientAPI(@RequestBody IdInvoiceRequest request,
                                                     HttpSession session) {
        return sellService.addNumberClientAPI(request, session);
    }

    @PostMapping(value = "sell/add-new-client")
    public ResponseEntity<String> addNewClientAPI(@RequestBody ClientRequest request) {
        return sellService.addNewClientAPI(request);
    }

    @PostMapping(value = "sell/add-voucher")
    public ResponseEntity<String> addVoucherForSellAPI(@RequestBody IdVoucherSellRequest request,
                                                       HttpSession session) {
        return sellService.addVoucherForSellAPI(request, session);
    }

    @PostMapping(value = "sell/add-product")
    public ResponseEntity<String> addProductForSellAPI(@RequestBody IdProductRequest request,
                                                       HttpSession session) {
        return sellService.addProductForSellAPI(request, session);
    }

    @PostMapping(value = "sell/reduce")
    public ResponseEntity<String> reduceQuantityProductSell(@RequestBody IdInvoiceDetailRequest request) {
        return sellService.reduceQuantityProductSell(request);
    }

    @PostMapping(value = "sell/increase")
    public ResponseEntity<String> increaseQuantityProductSell(@RequestBody IdInvoiceDetailRequest request) {
        return sellService.increaseQuantityProductSell(request);
    }

    @PostMapping(value = "sell/update-status")
    public ResponseEntity<String> updateStatusInvoice(@RequestBody InvoiceRequest request) throws IOException {
        return sellService.updateStatusInvoice(request);
    }
}
