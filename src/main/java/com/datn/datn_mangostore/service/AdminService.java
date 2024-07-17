package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.reponse.MonthlyRevenueResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.List;

public interface AdminService {
    String indexAdmin(Model model,
                      HttpSession session,
                      Integer years);

    List<MonthlyRevenueResponse> getMonthlyRevenue(Integer year);
}
