package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Invoice;
import com.datn.datn_mangostore.bean.InvoiceDetail;
import com.datn.datn_mangostore.bean.ProductDetail;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.reponse.InvoiceResponse;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.InvoiceDetailRepository;
import com.datn.datn_mangostore.repository.InvoiceRepository;
import com.datn.datn_mangostore.repository.ProductDetailRepository;
import com.datn.datn_mangostore.request.IdOrderRequest;
import com.datn.datn_mangostore.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final AccountRepository accountRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final ProductDetailRepository productDetailRepository;
    private final Gender gender;

    public OrderServiceImpl(AccountRepository accountRepository,
                            InvoiceRepository invoiceRepository,
                            InvoiceDetailRepository invoiceDetailRepository,
                            ProductDetailRepository productDetailRepository,
                            Gender gender) {
        this.accountRepository = accountRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.productDetailRepository = productDetailRepository;
        this.gender = gender;
    }

    @Override
    public String orderShow(Model model,
                            HttpSession session) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Invoice> itemsInvoice = invoiceRepository.findAllInvoiceOnline();
            model.addAttribute("listInvoiceOnline", itemsInvoice);
            return "admin/Order/IndexOrderShow";
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
                              HttpSession session,
                              String findByCode) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Invoice> findAllInvoice = invoiceRepository.getAllInvoiceOnline(detailAccount.getId());
            if (findByCode != null) {
                findAllInvoice = invoiceRepository.findInvoiceByCodeInvoice(detailAccount.getId(), findByCode);
                model.addAttribute("findByCode", findByCode);
            }

            model.addAttribute("listInvoice", findAllInvoice);
            return "admin/Order/OrderList";
        }
    }

    @Override
    public String detailOrderAdmin(Long idInvoice,
                                   Model model,
                                   HttpSession session) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
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

    @Override
    public String updateInvoiceStatusAdmin(Long idInvoice) {
        Invoice detailInvoice = invoiceRepository.findById(idInvoice).orElse(null);
        assert detailInvoice != null;
        int newStatusInvoice = detailInvoice.getInvoiceStatus() + 1;
        detailInvoice.setInvoiceStatus(newStatusInvoice);
        invoiceRepository.save(detailInvoice);
        return "redirect:/mangostore/admin/order/detail/" + idInvoice;
    }

    @Override
    public ResponseEntity<String> checkConfirmInvoice(IdOrderRequest request,
                                                      HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        Invoice detailInvoice = invoiceRepository.findById(Long.parseLong(request.getIdInvoice())).orElse(null);
        assert detailInvoice != null;
        if (Objects.equals(detailAccount.getId(), detailInvoice.getIdCustomer())) {
            return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
        } else {
            List<InvoiceDetail> getAllInvoiceDetail = invoiceDetailRepository.findAllByIdInvoice(Long.parseLong(request.getIdInvoice()));
            boolean hasError = false;

            for (InvoiceDetail detail : getAllInvoiceDetail) {
                ProductDetail productDetail = productDetailRepository.findById(detail.getProductDetail().getId()).orElse(null);
                if (productDetail == null) {
                    continue;
                }

                int quantityNew = productDetail.getQuantity() - detail.getQuantity();
                if (quantityNew < 0) {
                    hasError = true;
                    break;
                } else {
                    productDetail.setQuantity(quantityNew);
                    productDetailRepository.save(productDetail);
                }
            }

            if (hasError) {
                return new ResponseEntity<>("2", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("0", HttpStatus.OK);
            }
        }
    }

    @Override
    public String orderAllForManager(Model model,
                                     HttpSession session,
                                     String findByCode) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Invoice> findAllOrderByManager = invoiceRepository.findAllOrder();
            if (findByCode != null) {
                findAllOrderByManager = invoiceRepository.searchOrder(findByCode);
                model.addAttribute("findByCode", findByCode);
            }
            model.addAttribute("listOrderForManager", findAllOrderByManager);
            return "admin/Order/OrderManager";
        }
    }

    @Override
    public String orderAllDetailForManager(Model model,
                                           HttpSession session,
                                           Long idInvoice) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            Invoice detailInvoice = invoiceRepository.findById(idInvoice).orElse(null);
            assert detailInvoice != null;
            Account detailCustomByInvoice = accountRepository.findAccountByInvoice(detailInvoice.getIdCustomer());
            Account detailStaffByInvoice = accountRepository.findById(detailInvoice.getAccount().getId()).orElse(null);
            model.addAttribute("detailInvoice", detailInvoice);
            model.addAttribute("detailCustomByInvoice", detailCustomByInvoice);
            model.addAttribute("detailStaffByInvoice", detailStaffByInvoice);

            List<InvoiceDetail> itemsInvoiceDetail = invoiceDetailRepository.findAllInvoiceDetailByIdInvoice(idInvoice);
            model.addAttribute("listInvoiceDetail", itemsInvoiceDetail);
            return "admin/Order/OrderDetailManager";
        }
    }
}