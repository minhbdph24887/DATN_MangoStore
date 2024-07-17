package com.datn.datn_mangostore.controller;

import com.datn.datn_mangostore.service.CheckVnPayPaymentStatusService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class CheckVnPayPaymentStatus {
    private final CheckVnPayPaymentStatusService service;

    public CheckVnPayPaymentStatus(CheckVnPayPaymentStatusService service) {
        this.service = service;
    }

    @GetMapping(value = "/mangostore/banking/success")
    public String bankingSuccess(HttpServletRequest request,
                                 HttpSession session) throws IOException {
        return service.bankingSuccess(request, session);
    }
}
