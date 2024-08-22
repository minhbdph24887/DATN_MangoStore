package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.ProductDetail;
import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.bean.Voucher;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.reponse.MonthlyRevenueResponse;
import com.datn.datn_mangostore.repository.*;
import com.datn.datn_mangostore.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {
    private final AccountRepository accountRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final ProductDetailRepository productDetailRepository;
    private final VoucherRepository voucherRepository;
    private final Gender gender;

    public AdminServiceImpl(AccountRepository accountRepository,
                            InvoiceRepository invoiceRepository,
                            InvoiceDetailRepository invoiceDetailRepository,
                            ProductDetailRepository productDetailRepository,
                            VoucherRepository voucherRepository,
                            Gender gender) {
        this.accountRepository = accountRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.productDetailRepository = productDetailRepository;
        this.voucherRepository = voucherRepository;
        this.gender = gender;
    }

    @Override
    public String indexAdmin(Model model,
                             HttpSession session,
                             Integer years,
                             String precious,
                             String startDate,
                             String endDate) {
        Account detailAccount = gender.checkMenuAdmin(model, session, "Admin | Trang chủ");
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            Role detailRole = gender.findRoleByAccount(session);
            if (detailRole.getName().equals("ADMIN")) {
                Integer totalInvoiceComplete;
                Long totalPaymentOfAllInvoices;
                Integer totalInvoiceCancel;
                List<Object[]> results;
                if (years != null && precious != null && startDate == null && endDate == null) {
                    int startMonth = 1;
                    int endMonth = switch (precious) {
                        case "quyI" -> {
                            yield 3;
                        }
                        case "quyII" -> {
                            startMonth = 4;
                            yield 6;
                        }
                        case "quyIII" -> {
                            startMonth = 7;
                            yield 9;
                        }
                        case "quyIV" -> {
                            startMonth = 10;
                            yield 12;
                        }
                        default -> 3;
                    };
                    totalInvoiceComplete = invoiceRepository.countByInvoiceStatusAndYearAndQuarter(5, years, startMonth, endMonth);
                    totalPaymentOfAllInvoices = invoiceRepository.sumTotalPaymentByYearAndQuarter(years, startMonth, endMonth);
                    totalInvoiceCancel = invoiceRepository.countByInvoiceStatusCancelAndQuarter(years, startMonth, endMonth);
                    results = invoiceDetailRepository.findTopSellingProductsByYearAndQuarter(years, startMonth, endMonth);
                } else if (years != null && precious == null && startDate == null && endDate == null) {
                    totalInvoiceComplete = invoiceRepository.countByInvoiceStatusAndYear(5, years);
                    totalPaymentOfAllInvoices = invoiceRepository.sumTotalPaymentByYear(years);
                    totalInvoiceCancel = invoiceRepository.countByInvoiceStatusCancel(years);
                    results = invoiceDetailRepository.findTopSellingProductsByYear(years);
                } else if (startDate != null && endDate != null && years == null && precious == null) {
                    LocalDate start = LocalDate.parse(startDate);
                    LocalDate end = LocalDate.parse(endDate);

                    totalInvoiceComplete = invoiceRepository.countByInvoiceStatusAndDateRange(5, start, end);
                    totalPaymentOfAllInvoices = invoiceRepository.sumTotalPaymentByDateRange(start, end);
                    totalInvoiceCancel = invoiceRepository.countByInvoiceStatusAndDateRange(6, start, end);
                    results = invoiceDetailRepository.findTopSellingProductWithStartAndEndDate(start, end);
                } else {
                    LocalDate today = LocalDate.now();
                    totalInvoiceComplete = invoiceRepository.countByInvoiceStatusAndDate(5, today);
                    totalPaymentOfAllInvoices = invoiceRepository.sumTotalPaymentByDate(today);
                    totalInvoiceCancel = invoiceRepository.countByInvoiceStatusAndDate(6, today);
                    results = invoiceDetailRepository.findTopSellingProductsToday(today);
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
            } else {
                Integer totalInvoiceCompleteForStaff;
                Long totalPaymentOfAllInvoicesForStaff;
                Integer totalInvoiceCancelForStaff;

                if (startDate != null && endDate != null) {
                    LocalDate start = LocalDate.parse(startDate);
                    LocalDate end = LocalDate.parse(endDate);

                    totalInvoiceCompleteForStaff = invoiceRepository.countByInvoiceStatusAndDateRangeByStaff(5, detailAccount.getId(), start, end);
                    totalPaymentOfAllInvoicesForStaff = invoiceRepository.sumTotalPaymentByDateRangeByStaff(start, detailAccount.getId(), end);
                    totalInvoiceCancelForStaff = invoiceRepository.countByInvoiceStatusAndDateRangeByStaff(6, detailAccount.getId(), start, end);
                } else {
                    LocalDate today = LocalDate.now();
                    totalInvoiceCompleteForStaff = invoiceRepository.countByInvoiceStatusAndDateByStaff(5, detailAccount.getId(), today);
                    totalPaymentOfAllInvoicesForStaff = invoiceRepository.sumTotalPaymentByDateByStaff(detailAccount.getId(), today);
                    totalInvoiceCancelForStaff = invoiceRepository.countByInvoiceStatusAndDateByStaff(6, detailAccount.getId(), today);
                }
                model.addAttribute("totalInvoiceCompleted", totalInvoiceCompleteForStaff);
                model.addAttribute("totalPaymentOfAllInvoices", totalPaymentOfAllInvoicesForStaff != null ? totalPaymentOfAllInvoicesForStaff : 0);
                model.addAttribute("totalInvoiceCancel", totalInvoiceCancelForStaff);
                return "admin/IndexStaff";
            }
        }
    }

    @Override
    public List<MonthlyRevenueResponse> getMonthlyRevenue(HttpSession session,
                                                          Integer year,
                                                          String quarter,
                                                          String startDate,
                                                          String endDate) {
        Role detailRoleByAccount = gender.findRoleByAccount(session);
        if (detailRoleByAccount.getName().equals("ADMIN")) {
            List<Object[]> results;
            int queryYear = (year != null) ? year : Year.now().getValue();
            if (year != null && quarter != null && startDate == null && endDate == null) {
                int startMonth;
                int endMonth = switch (quarter) {
                    case "quyI" -> {
                        startMonth = 1;
                        yield 3;
                    }
                    case "quyII" -> {
                        startMonth = 4;
                        yield 6;
                    }
                    case "quyIII" -> {
                        startMonth = 7;
                        yield 9;
                    }
                    case "quyIV" -> {
                        startMonth = 10;
                        yield 12;
                    }
                    default -> {
                        startMonth = 1;
                        yield 12;
                    }
                };
                results = invoiceRepository.findQuarterlyRevenueByYear(queryYear, startMonth, endMonth);
            } else if (year != null && startDate == null && endDate == null) {
                results = invoiceRepository.findMonthlyRevenueByYear(queryYear);
            } else if (startDate != null && endDate != null && year == null && quarter == null) {
                LocalDate start = LocalDate.parse(startDate);
                LocalDate end = LocalDate.parse(endDate);
                results = invoiceRepository.findDailyRevenueByDateRange(start, end);
            } else {
                LocalDate today = LocalDate.now();
                results = invoiceRepository.findDailyRevenueByDate(today);
            }
            List<MonthlyRevenueResponse> monthlyRevenueList = new ArrayList<>();
            for (Object[] result : results) {
                int month = ((Number) result[0]).intValue();
                int dayOfMonth = (Integer) result[0];
                Integer totalRevenue = ((Number) result[1]).intValue();
                MonthlyRevenueResponse dto;
                if (year != null && quarter != null) {
                    dto = new MonthlyRevenueResponse(null, month, totalRevenue);
                } else if (year != null) {
                    dto = new MonthlyRevenueResponse(null, month, totalRevenue);
                } else {
                    LocalDate date = LocalDate.now().withDayOfMonth(dayOfMonth);
                    dto = new MonthlyRevenueResponse(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()), 0, totalRevenue);
                }
                monthlyRevenueList.add(dto);
            }
            return monthlyRevenueList;
        } else {
            String email = (String) session.getAttribute("loginEmail");
            assert email != null;
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            List<Object[]> results;
            if (startDate != null && endDate != null) {
                LocalDate start = LocalDate.parse(startDate);
                LocalDate end = LocalDate.parse(endDate);
                results = invoiceRepository.findDailyRevenueByDateRangeByStaff(detailAccount.getId(), start, end);
            } else {
                LocalDate today = LocalDate.now();
                results = invoiceRepository.findDailyRevenueByDateByStaff(detailAccount.getId(), today);
            }

            List<MonthlyRevenueResponse> dailyRevenueList = new ArrayList<>();
            for (Object[] result : results) {
                int dayOfMonth = (Integer) result[0];
                Integer totalRevenue = ((Number) result[1]).intValue();

                LocalDate date = LocalDate.now().withDayOfMonth(dayOfMonth);

                MonthlyRevenueResponse dto = new MonthlyRevenueResponse(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()), 0, totalRevenue);
                dailyRevenueList.add(dto);
            }
            return dailyRevenueList;
        }
    }
}
