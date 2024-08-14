package com.datn.datn_mangostore.controller;

import com.datn.datn_mangostore.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping(value = "/mangostore/login/")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping(value = "from")
    public String loginFrom(Model model) {
        model.addAttribute("titleWebsite", "MangoStore | Đăng nhập");
        return "login/Form";
    }

    @PostMapping(value = "success")
    public String loginSuccess(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               HttpSession session) throws IOException {
        return loginService.loginAccount(email, password, session);
    }

    @GetMapping(value = "forgot")
    public String viewForgot(Model model) {
        model.addAttribute("titleWebsite", "MangoStore | Quên mật khẩu");
        return "login/ForgotPassword";
    }

    @PostMapping(value = "code")
    public String veryCodeEmail(@RequestParam("forgotEmail") String email,
                                HttpSession session) {
        session.setAttribute("loginEmailForgot", email);
        return loginService.forgotEmail(email);
    }

    @PostMapping(value = "forgot/password")
    public String authenticationPassword(@RequestParam("codeForgot") String codeForgot,
                                         HttpSession session) {
        return loginService.authenticationCode(codeForgot, session);
    }

    @GetMapping(value = "password/refresh")
    public String refreshPassword(HttpSession session,
                                  Model model) {
        model.addAttribute("titleWebsite", "MangoStore | Đặt mật khẩu");
        String email = (String) session.getAttribute("loginEmailForgot");
        if (email == null) {
            return "login/ForgotPassword";
        } else {
            return "login/NewPassword";
        }
    }

    @PostMapping(value = "refresh/success")
    public String refreshPasswordSuccess(@RequestParam("passwordRefresh") String passwordRefresh,
                                         HttpSession session) {
        String email = (String) session.getAttribute("loginEmailForgot");
        return loginService.refreshPassword(email, passwordRefresh, session);
    }

    @GetMapping(value = "signup")
    public String signUpAccount(Model model) {
        model.addAttribute("titleWebsite", "MangoStore | Đăng ký");
        return "login/SignUp";
    }

    @PostMapping(value = "signup/success")
    public String signUpAccountSuccess(@RequestParam("fullName") String fullName,
                                       @RequestParam("email") String email,
                                       @RequestParam("passwordRefresh") String passwordRefresh) {
        return loginService.signUpAccount(fullName, email, passwordRefresh);
    }

    @GetMapping(value = "clear")
    public String logOutWebsite(HttpSession session) {
        return loginService.logOutWebsite(session);
    }
}
