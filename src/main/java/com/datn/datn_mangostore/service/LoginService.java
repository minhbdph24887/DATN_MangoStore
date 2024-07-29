package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.request.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface LoginService {
    String loginAccount(String email,
                        String password,
                        HttpSession session) throws IOException;

    void checkLoginGoogleAccount(HttpServletResponse response,
                                 Authentication authentication) throws IOException;

    String forgotEmail(String email);

    String authenticationCode(String codeForgot,
                              HttpSession session);

    String refreshPassword(String email,
                           String passwordRefresh,
                           HttpSession session);

    String signUpAccount(String fullName,
                         String email,
                         String passwordRefresh);

    String logOutWebsite(HttpSession session);

    ResponseEntity<String> checkLogin(LoginRequest request);

    ResponseEntity<String> checkRegister(LoginRequest request);

    ResponseEntity<String> checkSendEmail(LoginRequest request);

    ResponseEntity<String> checkForgotPassword(LoginRequest request,
                                               HttpSession session);
}
