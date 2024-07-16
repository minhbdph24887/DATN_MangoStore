package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.bean.VoucherClient;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.RoleRepository;
import com.datn.datn_mangostore.repository.VoucherClientRepository;
import com.datn.datn_mangostore.service.VoucherClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VoucherClientServiceImpl implements VoucherClientService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final VoucherClientRepository voucherClientRepository;

    public VoucherClientServiceImpl(AccountRepository accountRepository,
                                    RoleRepository roleRepository,
                                    VoucherClientRepository voucherClientRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.voucherClientRepository = voucherClientRepository;
    }

    @Override
    public String indexVoucherClient(Model model, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);

            Role detailRoleByEmail = roleRepository.getRoleByEmail(email);

            if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                model.addAttribute("checkAuthentication", detailRoleByEmail);
            }
            List<VoucherClient> itemsVoucherClient = voucherClientRepository.findAll();
            model.addAttribute("listVoucherClient", itemsVoucherClient);
        }
        return "admin/voucherClient/IndexVoucherClient";
    }

    @Override
    public String detailVoucherClient(Model model,
                                      HttpSession session,
                                      Long idVoucherClient) {
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

                VoucherClient detailVoucher = voucherClientRepository.findById(idVoucherClient).orElse(null);
                model.addAttribute("voucherClient", detailVoucher);
                return "admin/voucherClient/DetailVoucherClient";
            }
        }
    }

    @Override
    public String removeVoucherClient(Long idVoucherClient) {
        VoucherClient detailVoucherClient = voucherClientRepository.findById(idVoucherClient).orElse(null);
        assert detailVoucherClient != null;
        detailVoucherClient.setStatus(0);
        voucherClientRepository.save(detailVoucherClient);
        return "redirect:/mangostore/voucher-wallet";
    }
}
