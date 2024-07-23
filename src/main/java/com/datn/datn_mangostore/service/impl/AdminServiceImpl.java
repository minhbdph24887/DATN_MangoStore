package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.ProductDetail;
import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.bean.Voucher;
import com.datn.datn_mangostore.reponse.MonthlyRevenueResponse;
import com.datn.datn_mangostore.repository.*;
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
    private final ProductDetailRepository productDetailRepository;
    private final VoucherRepository voucherRepository;

    public AdminServiceImpl(AccountRepository accountRepository,
                            RoleRepository roleRepository,
                            InvoiceRepository invoiceRepository,
                            InvoiceDetailRepository invoiceDetailRepository,
                            ProductDetailRepository productDetailRepository, VoucherRepository voucherRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.productDetailRepository = productDetailRepository;
        this.voucherRepository = voucherRepository;
    }

    @Override
    public String indexAdmin(Model model,
                             HttpSession session,
                             Integer years,
                             String precious) {
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
                model.addAttribute("checkMenuAdmin", detailRole.getName().equals("ADMIN"));

                Integer totalInvoiceComplete;
                Long totalPaymentOfAllInvoices;
                Integer totalInvoiceCancel;
                List<Object[]> results;
                if (years != null && precious != null) {
                    int startMonth = 1;
                    int endMonth = 3;
                    switch (precious) {
                        case "quyI":
                            startMonth = 1;
                            endMonth = 3;
                            break;
                        case "quyII":
                            startMonth = 4;
                            endMonth = 6;
                            break;
                        case "quyIII":
                            startMonth = 7;
                            endMonth = 9;
                            break;
                        case "quyIV":
                            startMonth = 10;
                            endMonth = 12;
                            break;
                    }
                    totalInvoiceComplete = invoiceRepository.countByInvoiceStatusAndYearAndQuarter(5, years, startMonth, endMonth);
                    totalPaymentOfAllInvoices = invoiceRepository.sumTotalPaymentByYearAndQuarter(years, startMonth, endMonth);
                    totalInvoiceCancel = invoiceRepository.countByInvoiceStatusCancelAndQuarter(years, startMonth, endMonth);
                    results = invoiceDetailRepository.findTopSellingProductsByYearAndQuarter(years, startMonth, endMonth);
                } else if (years != null) {
                    totalInvoiceComplete = invoiceRepository.countByInvoiceStatusAndYear(5, years);
                    totalPaymentOfAllInvoices = invoiceRepository.sumTotalPaymentByYear(years);
                    totalInvoiceCancel = invoiceRepository.countByInvoiceStatusCancel();
                    results = invoiceDetailRepository.findTopSellingProductsByYear(years);
                } else {
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

                List<ProductDetail> listProductDetailWithQuantity = productDetailRepository.findAllProductByQuantity();
                model.addAttribute("listProductDetailWithQuantity", listProductDetailWithQuantity);

                List<Voucher> listVoucherByQuantity = voucherRepository.findAlLVoucherByQuantity();
                model.addAttribute("listVoucherByQuantity", listVoucherByQuantity);
                return "admin/Index";
            }
        }
    }

    @Override
    public List<MonthlyRevenueResponse> getMonthlyRevenue(Integer year, String quarter) {
        List<Object[]> results;

        // Determine the year to use: either the provided year or the current year
        int queryYear = (year != null) ? year : Year.now().getValue();
        if (year != null && quarter != null) {
            // If both year and quarter are provided, fetch data for the specific quarter
            int startMonth;
            int endMonth;
            switch (quarter) {
                case "quyI":
                    startMonth = 1;
                    endMonth = 3;
                    break;
                case "quyII":
                    startMonth = 4;
                    endMonth = 6;
                    break;
                case "quyIII":
                    startMonth = 7;
                    endMonth = 9;
                    break;
                case "quyIV":
                    startMonth = 10;
                    endMonth = 12;
                    break;
                default:
                    startMonth = 1;
                    endMonth = 12;
                    break;
            }
            results = invoiceRepository.findQuarterlyRevenueByYear(queryYear, startMonth, endMonth);
        } else if (year != null) {
            // If only year is provided, fetch data for all months of that year
            results = invoiceRepository.findMonthlyRevenueByYear(queryYear);
        } else {
            // If no parameters are provided, fetch data for the current year
            results = invoiceRepository.findMonthlyRevenueByYear(queryYear);
        }

        // Convert the results to a list of MonthlyRevenueResponse
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
