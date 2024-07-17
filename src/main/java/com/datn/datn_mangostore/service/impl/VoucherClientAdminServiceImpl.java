package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.bean.VoucherClient;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.RankRepository;
import com.datn.datn_mangostore.repository.RoleRepository;
import com.datn.datn_mangostore.repository.VoucherClientRepository;
import com.datn.datn_mangostore.service.VoucherClientAdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VoucherClientAdminServiceImpl implements VoucherClientAdminService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final VoucherClientRepository voucherClientRepository;
    private final RankRepository rankRepository;

    public VoucherClientAdminServiceImpl(AccountRepository accountRepository,
                                         RoleRepository roleRepository, VoucherClientRepository voucherClientRepository,
                                         RankRepository rankRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.voucherClientRepository = voucherClientRepository;
        this.rankRepository = rankRepository;
    }


    @Override
    public String indexVoucherClient(Model model, HttpSession session) {
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

                List<VoucherClient> lvc = voucherClientRepository.getAllVoucherClientByVoucherStatus1and2();
                model.addAttribute("listVoucherClient", lvc);

                List<VoucherClient> findAllVoucherClientByStatus0 = voucherClientRepository.getAllVoucherClientByVoucherStatus0();
                model.addAttribute("listVoucherClientAdminInactive", findAllVoucherClientByStatus0);
                return "admin/voucherClient/IndexVoucherClient";
            }
        }
    }
}
