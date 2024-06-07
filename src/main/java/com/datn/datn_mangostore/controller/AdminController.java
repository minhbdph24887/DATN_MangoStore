package com.datn.datn_mangostore.controller;

import com.datn.datn_mangostore.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/mangostore/admin/")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping(value = "home")
    public String indexAdmin(Model model,
                             HttpSession session) {
        return adminService.indexAdmin(model, session);
    }
}
