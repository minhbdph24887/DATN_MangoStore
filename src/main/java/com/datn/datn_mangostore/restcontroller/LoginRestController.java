package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.request.LoginRequest;
import com.datn.datn_mangostore.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/mangostore/login/")
public class LoginRestController {
    private final LoginService loginService;

    public LoginRestController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(value = "check-login")
    public ResponseEntity<String> checkLogin(@RequestBody LoginRequest request) {
        return loginService.checkLogin(request);
    }

    @PostMapping(value = "check-register")
    public ResponseEntity<String> checkRegister(@RequestBody LoginRequest request) {
        return loginService.checkRegister(request);
    }

    @PostMapping(value = "check-send-email")
    public ResponseEntity<String> checkSendEmail(@RequestBody LoginRequest request) {
        return loginService.checkSendEmail(request);
    }

    @PostMapping(value = "check-forgot-password")
    public ResponseEntity<String> checkForgotPassword(@RequestBody LoginRequest request,
                                                      HttpSession session) {
        return loginService.checkForgotPassword(request, session);
    }
}
