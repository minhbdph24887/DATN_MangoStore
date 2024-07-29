package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.reponse.InvoiceResponse;
import com.datn.datn_mangostore.request.IdOrderRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface OrderService {
    String orderShow(Model model,
                     HttpSession session);

    ResponseEntity<InvoiceResponse> detailsInvoiceByIdForAdmin(Long id);

    String confirmInvoice(Long idInvoice,
                          HttpSession session);

    String listInvoice(Model model,
                       HttpSession session,
                       String findByCode);

    String detailOrderAdmin(Long idInvoice,
                            Model model,
                            HttpSession session);

    String updateInvoiceStatusAdmin(Long idInvoice);

    ResponseEntity<String> checkConfirmInvoice(IdOrderRequest request,
                                               HttpSession session);

    String orderAllForManager(Model model,
                              HttpSession session,
                              String findByCode);

    String orderAllDetailForManager(Model model,
                                    HttpSession session,
                                    Long idInvoice);
}
