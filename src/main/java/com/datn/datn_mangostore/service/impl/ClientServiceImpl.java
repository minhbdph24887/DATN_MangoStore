package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.RoleRepository;
import com.datn.datn_mangostore.service.ClientService;
import jakarta.servlet.http.HttpSession;
import liquibase.pro.packaged.C;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class ClientServiceImpl implements ClientService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public ClientServiceImpl(AccountRepository accountRepository,
                             RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public String indexClient(Model model,
                              HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email != null) {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);
            Role detailRoleByEmail = roleRepository.getRoleByEmail(email);
            if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                model.addAttribute("checkAuthentication", detailRoleByEmail);
            }
        }
        return "client/Home";
    }
}
