package com.datn.datn_mangostore.controller;

import com.datn.datn_mangostore.reponse.MonthlyRevenueResponse;
import com.datn.datn_mangostore.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/mangostore/admin/")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping(value = "home")
    public String indexAdmin(Model model,
                             HttpSession session,
                             @RequestParam(value = "fillerByYears", required = false) Integer years) {
        return adminService.indexAdmin(model, session, years);
    }

    @GetMapping(value = "monthly-revenue")
    @ResponseBody
    public List<MonthlyRevenueResponse> getMonthlyRevenue(@Param("year") Integer year) {
        return adminService.getMonthlyRevenue(year);
    }
}
