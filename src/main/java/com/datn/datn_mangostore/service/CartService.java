package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.bean.AddressClient;
import com.datn.datn_mangostore.reponse.InvoiceResponse;
import com.datn.datn_mangostore.request.CodeVoucherRequest;
import com.datn.datn_mangostore.request.QuantityCartRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface CartService {
    String indexCart(Model model,
                     HttpSession session);

    String reduceQuantity(Long idShoppingCartDetail);

    ResponseEntity<String> checkIncreaseQuantityForCart(QuantityCartRequest request);

    String increaseQuantity(Long idShoppingCartDetail);

    String deleteProductCart(Long idShoppingCartDetail);

    String viewCheckOut(Model model,
                        HttpSession session);

    String addAddressClientForClient(AddressClient newAddressClient,
                                     HttpSession session);

    String updateStatusClientAddress(Long id,
                                     HttpSession session);

    String updateAddressClientForCheckOut(AddressClient editAddressClient);

    String addPointClientToInvoice(HttpSession session);

    String addVoucherToInvoice(Long id,
                               HttpSession session);

    ResponseEntity<String> addVoucherByCodeVoucher(CodeVoucherRequest request,
                                                   HttpSession session);

    ResponseEntity<String> updateCashInvoice(Long idInvoice);

    ResponseEntity<InvoiceResponse> detailInvoiceById(Long id);

    String bankingVnPay(Long idInvoice,
                        HttpServletRequest request,
                        HttpSession session);
}
