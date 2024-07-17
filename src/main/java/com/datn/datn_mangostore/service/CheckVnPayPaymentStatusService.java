package com.datn.datn_mangostore.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public interface CheckVnPayPaymentStatusService {
    String bankingSuccess(HttpServletRequest request,
                          HttpSession session) throws IOException;
}
