package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.reponse.MonthlyRevenueResponse;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.InvoiceDetailRepository;
import com.datn.datn_mangostore.repository.InvoiceRepository;
import com.datn.datn_mangostore.repository.RoleRepository;
import com.datn.datn_mangostore.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;

    public AdminServiceImpl(AccountRepository accountRepository,
                            RoleRepository roleRepository, InvoiceRepository invoiceRepository, InvoiceDetailRepository invoiceDetailRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
    }

    @Override
    public String indexAdmin(Model model,
                             HttpSession session,
                             Integer years) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            if (detailAccount.getStatus() == 0) {
                session.invalidate();
                return "redirect:/mangostore/home";
            } else {
                model.addAttribute("profile", detailAccount);

                LocalDateTime checkDate = LocalDateTime.now();
                int hour = checkDate.getHour();
                if (hour >= 5 && hour < 10) {
                    model.addAttribute("dates", "Morning");
                } else if (hour >= 10 && hour < 13) {
                    model.addAttribute("dates", "Noon");
                } else if (hour >= 13 && hour < 18) {
                    model.addAttribute("dates", "Afternoon");
                } else {
                    model.addAttribute("dates", "Evening");
                }

                Role detailRole = roleRepository.getRoleByEmail(email);
                if (detailRole.getName().equals("ADMIN")) {
                    model.addAttribute("checkMenuAdmin", true);
                } else {
                    model.addAttribute("checkMenuAdmin", false);
                }

                Integer totalInvoiceComplete;
                Long totalPaymentOfAllInvoices;
                Integer totalInvoiceCancel;
                List<Object[]> results;
                if(years != null){
                    totalInvoiceComplete = invoiceRepository.countByInvoiceStatusAndYear(5, years);
                    totalPaymentOfAllInvoices = invoiceRepository.sumTotalPaymentByYear(years);
                    totalInvoiceCancel = invoiceRepository.countByInvoiceStatusCancel();
                    results = invoiceDetailRepository.findTopSellingProductsByYear(years);
                }else{
                    Integer yearNow = Year.now().getValue();
                    totalInvoiceComplete = invoiceRepository.countByInvoiceStatusAndYear(5, yearNow);
                    totalPaymentOfAllInvoices = invoiceRepository.sumTotalPaymentByYear(yearNow);
                    totalInvoiceCancel = invoiceRepository.countByInvoiceStatusCancel();
                    results = invoiceDetailRepository.findTopSellingProductsByYear(yearNow);
                }

                model.addAttribute("totalInvoiceCompleted", totalInvoiceComplete);
                model.addAttribute("totalPaymentOfAllInvoices", totalPaymentOfAllInvoices != null ? totalPaymentOfAllInvoices : 0);
                model.addAttribute("totalInvoiceCancel", totalInvoiceCancel);
                List<Map<String, Object>> topSellingProducts = new ArrayList<>();
                for (Object[] result : results) {
                    Map<String, Object> productData = new HashMap<>();
                    productData.put("productDetailId", result[0]);
                    productData.put("totalQuantity", result[1]);
                    productData.put("productName", result[2]);
                    productData.put("sizeName", result[3]);
                    productData.put("colorName", result[4]);
                    productData.put("imagePath", result[5]);
                    topSellingProducts.add(productData);
                }

                model.addAttribute("topSellingProducts", topSellingProducts);
                return "admin/Index";
            }
        }
    }

    @Override
    public List<MonthlyRevenueResponse> getMonthlyRevenue(Integer year) {
        List<Object[]> results;
        if (year != null) {
            results = invoiceRepository.findMonthlyRevenueByYear(year);
        } else {
            int currentYear = Year.now().getValue();
            results = invoiceRepository.findMonthlyRevenueByYear(currentYear);
        }
        List<MonthlyRevenueResponse> monthlyRevenueList = new ArrayList<>();

        for (Object[] result : results) {
            int month = ((Number) result[0]).intValue();
            Integer totalRevenue = ((Number) result[1]).intValue();
            MonthlyRevenueResponse dto = new MonthlyRevenueResponse(month, totalRevenue);
            monthlyRevenueList.add(dto);
        }
        return monthlyRevenueList;
    }
}
