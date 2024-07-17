package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Invoice;
import com.datn.datn_mangostore.bean.InvoiceDetail;
import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.reponse.InvoiceResponse;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.InvoiceDetailRepository;
import com.datn.datn_mangostore.repository.InvoiceRepository;
import com.datn.datn_mangostore.repository.RoleRepository;
import com.datn.datn_mangostore.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;

    public OrderServiceImpl(AccountRepository accountRepository,
                            RoleRepository roleRepository,
                            InvoiceRepository invoiceRepository,
                            InvoiceDetailRepository invoiceDetailRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
    }

    @Override
    public String orderShow(Model model,
                            HttpSession session) {
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

                List<Invoice> itemsInvoice = invoiceRepository.findAllInvoiceOnline();
                model.addAttribute("listInvoiceOnline", itemsInvoice);
                return "admin/Order/IndexOrderShow";
            }
        }
    }

    @Override
    public ResponseEntity<InvoiceResponse> detailsInvoiceByIdForAdmin(Long id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if (invoice.isPresent()) {
            Invoice detailInvoice = invoice.get();
            Account account = accountRepository.findByCodeInvoice(detailInvoice.getCodeInvoice());
            List<InvoiceDetail> invoiceDetails = invoiceDetailRepository.findAllByIdInvoice(id);
            InvoiceResponse invoiceResponse = new InvoiceResponse(detailInvoice, account, invoiceDetails);
            invoiceResponse.setId(id);
            return ResponseEntity.ok(invoiceResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public String confirmInvoice(Long idInvoice,
                                 HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        assert email != null;
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        Invoice detailInvoice = invoiceRepository.findById(idInvoice).orElse(null);
        assert detailInvoice != null;
        detailInvoice.setAccount(detailAccount);
        detailInvoice.setInvoiceStatus(2);
        invoiceRepository.save(detailInvoice);
        return "redirect:/mangostore/admin/order/show";
    }

    @Override
    public String listInvoice(Model model,
                              HttpSession session) {
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


                List<Invoice> findAllInvoice = invoiceRepository.getAllInvoiceOnline(detailAccount.getId());
                model.addAttribute("listInvoice", findAllInvoice);
                return "admin/Order/OrderList";
            }
        }
    }

    @Override
    public String detailOrderAdmin(Long idInvoice,
                                   Model model,
                                   HttpSession session) {
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

                Invoice detailInvoice = invoiceRepository.findById(idInvoice).orElse(null);
                assert detailInvoice != null;
                Account detailAccountByInvoice = accountRepository.findAccountByInvoice(detailInvoice.getIdCustomer());
                model.addAttribute("detailInvoice", detailInvoice);
                model.addAttribute("detailAccountByInvoice", detailAccountByInvoice);

                List<InvoiceDetail> itemsInvoiceDetail = invoiceDetailRepository.findAllInvoiceDetailByIdInvoice(idInvoice);
                model.addAttribute("listInvoiceDetail", itemsInvoiceDetail);
                return "admin/Order/OrderDetail";
            }
        }
    }

    @Override
    public String updateInvoiceStatusAdmin(Long idInvoice) {
        Invoice detailInvoice = invoiceRepository.findById(idInvoice).orElse(null);
        assert detailInvoice != null;
        int newStatusInvoice = detailInvoice.getInvoiceStatus() + 1;
        detailInvoice.setInvoiceStatus(newStatusInvoice);
        invoiceRepository.save(detailInvoice);
        return "redirect:/mangostore/admin/order/detail/" + idInvoice;
    }
}