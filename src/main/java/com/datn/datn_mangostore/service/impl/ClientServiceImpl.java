package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Invoice;
import com.datn.datn_mangostore.bean.VoucherClient;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.InvoiceRepository;
import com.datn.datn_mangostore.repository.VoucherClientRepository;
import com.datn.datn_mangostore.request.ProfileRequest;
import com.datn.datn_mangostore.service.ClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final VoucherClientRepository voucherClientRepository;
    private final InvoiceRepository invoiceRepository;
    private final Gender gender;

    public ClientServiceImpl(AccountRepository accountRepository,
                             PasswordEncoder encoder,
                             VoucherClientRepository voucherClientRepository,
                             InvoiceRepository invoiceRepository,
                             Gender gender) {
        this.accountRepository = accountRepository;
        this.encoder = encoder;
        this.voucherClientRepository = voucherClientRepository;
        this.invoiceRepository = invoiceRepository;
        this.gender = gender;
    }

    @Override
    public String indexClient(Model model,
                              HttpSession session) {
        Account detailAccount = gender.checkMenuClient(model, session, "Mango Store | Trang Chủ");
        assert detailAccount != null;
        return "client/Home";
    }

    @Override
    public String indexProfile(Model model,
                               HttpSession session) {
        Account detailAccount = gender.checkMenuClient(model, session, "Mango Store | Tài khoản");
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            return "client/Profile/DetailProfile/ProfileClient";
        }
    }

    @Override
    public ResponseEntity<String> checkUpdateProfile(ProfileRequest request) {
        Account currentAccount = accountRepository.findById(Long.parseLong(request.getId())).orElse(null);
        assert currentAccount != null;

        if (!request.getNumberPhone().equals(currentAccount.getNumberPhone())) {
            Account detailAccountByNumberPhone = accountRepository.findAccountByNumberPhone(Integer.parseInt(request.getNumberPhone()));
            if (detailAccountByNumberPhone != null) {
                return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
            }
        }

        if (!request.getEmail().equals(currentAccount.getEmail())) {
            Account detailAccountByEmail = accountRepository.detailAccountByEmail(request.getEmail());
            if (detailAccountByEmail != null) {
                return new ResponseEntity<>("2", HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>("0", HttpStatus.OK);
    }

    @Override
    public String updateProfile(Account profile,
                                MultipartFile imageFile,
                                HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            Account updateAccount = accountRepository.findById(detailAccount.getId()).orElse(null);
            assert updateAccount != null;
            updateAccount.setFullName(profile.getFullName());
            updateAccount.setNumberPhone(profile.getNumberPhone());
            updateAccount.setEmail(profile.getEmail());
            updateAccount.setGender(profile.getGender());
            updateAccount.setBirthday(profile.getBirthday());

            String oldImagePath = accountRepository.findById(detailAccount.getId()).get().getImages();
            if (imageFile.isEmpty()) {
                updateAccount.setImages(oldImagePath);
            } else {
                String fileName = imageFile.getOriginalFilename();
                updateAccount.setImages(fileName);
            }
            accountRepository.save(updateAccount);
        }
        return "redirect:/mangostore/profile";
    }

    @Override
    public String changePassword(Model model,
                                 HttpSession session) {
        Account detailAccount = gender.checkMenuClient(model, session, "Mango Store | Mật khẩu");
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            return "client/Profile/DetailProfile/ChangePassword";
        }
    }

    @Override
    public String updateChangePassword(Account profile,
                                       HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            Account updateAccount = accountRepository.findById(detailAccount.getId()).orElse(null);
            assert updateAccount != null;
            updateAccount.setEncryptionPassword(encoder.encode(profile.getEncryptionPassword()));
            accountRepository.save(updateAccount);
        }
        return "redirect:/mangostore/change-password";
    }

    @Override
    public String indexVoucherClient(Model model,
                                     HttpSession session) {
        Account detailAccount = gender.checkMenuClient(model, session, "Mango Store | Phiếu giảm giá");
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<VoucherClient> itemsVoucherClient = voucherClientRepository.findAllVoucherForClient(detailAccount.getRank().getId(), detailAccount.getId());
            model.addAttribute("listVoucherClient", itemsVoucherClient);
            return "client/Profile/DetailProfile/VoucherClient";
        }
    }

    @Override
    public String indexViewPurchase(Model model,
                                    HttpSession session,
                                    String status,
                                    Integer pageNo,
                                    Integer pageSize) {
        Account detailAccount = gender.checkMenuClient(model, session, "Mango Store | Hóa đơn");
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            Page<Invoice> itemsAllInvoiceOnline;
            PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize);
            itemsAllInvoiceOnline = switch (status) {
                case "waitForConfirmation" ->
                        invoiceRepository.findAllInvoiceOnline(detailAccount.getId(), 1, pageRequest);
                case "confirmed" -> invoiceRepository.findAllInvoiceOnline(detailAccount.getId(), 2, pageRequest);
                case "deliveredToDVVC" -> invoiceRepository.findAllInvoiceOnline(detailAccount.getId(), 3, pageRequest);
                case "areDelivering" -> invoiceRepository.findAllInvoiceOnline(detailAccount.getId(), 4, pageRequest);
                case "success" -> invoiceRepository.findAllInvoiceOnline(detailAccount.getId(), 5, pageRequest);
                case "cancelled" -> invoiceRepository.findAllInvoiceOnline(detailAccount.getId(), 6, pageRequest);
                default -> invoiceRepository.findAllInvoiceOnlineByAllStatus(detailAccount.getId(), pageRequest);
            };
            model.addAttribute("listInvoice", itemsAllInvoiceOnline);
            model.addAttribute("totalPage", itemsAllInvoiceOnline.getTotalPages());
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("status", status);

            int startCount = (pageNo - 1) * pageSize;
            model.addAttribute("startCount", startCount);
            return "client/Profile/detailProfile/IndexPurchase";
        }
    }

    @Override
    public String cancelPurchase(Long idInvoice) {
        Invoice detailInvoice = invoiceRepository.findById(idInvoice).orElse(null);
        assert detailInvoice != null;
        detailInvoice.setInvoiceStatus(6);
        invoiceRepository.save(detailInvoice);
        return "redirect:/mangostore/purchase";
    }
}