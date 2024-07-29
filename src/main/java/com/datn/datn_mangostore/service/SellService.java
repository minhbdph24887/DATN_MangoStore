package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.request.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.io.IOException;

public interface SellService {
    String indexSellAdmin(Model model,
                          HttpSession session);

    ResponseEntity<String> createInvoiceAPI(HttpSession session);

    String editInvoice(Long idInvoice,
                       Model model,
                       HttpSession session);

    ResponseEntity<String> addNumberClientAPI(IdInvoiceRequest request,
                                              HttpSession session);

    String updatePoint(Long idInvoice,
                       Integer pointClient);

    String cancelInvoice(Long idInvoice);

    ResponseEntity<String> addVoucherListForSellAPI(VoucherSellRequest request);

    ResponseEntity<String> addVoucherCodeForSellAPI(VoucherSellRequest request);

    ResponseEntity<String> addProductForSellAPI(IdProductRequest request,
                                                HttpSession session);

    ResponseEntity<String> reduceQuantityProductSell(IdInvoiceDetailRequest request);

    ResponseEntity<String> increaseQuantityProductSell(IdInvoiceDetailRequest request);

    String deleteProduct(Long idInvoiceDetail);

    ResponseEntity<String> updateStatusInvoice(InvoiceRequest request) throws IOException;

    String paymentVnPay(Long idInvoice,
                        HttpServletRequest request,
                        HttpSession session);

    ResponseEntity<String> addNewClientAPI(ClientRequest request);
}