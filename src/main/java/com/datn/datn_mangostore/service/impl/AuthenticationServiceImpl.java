package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Authentication;
import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.AuthenticationRepository;
import com.datn.datn_mangostore.repository.RoleRepository;
import com.datn.datn_mangostore.request.IdAccountRequest;
import com.datn.datn_mangostore.service.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationRepository authenticationRepository;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public AuthenticationServiceImpl(AuthenticationRepository authenticationRepository, AccountRepository accountRepository, RoleRepository roleRepository) {
        this.authenticationRepository = authenticationRepository;
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public String getAllRole(Model model, HttpSession session) {
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

                List<Authentication> itemsAuthentication = authenticationRepository.findAll();
                model.addAttribute("listAuthentication", itemsAuthentication);

                model.addAttribute("checkMenuAdmin", true);
                return "admin/authentication/IndexAuthentication";
            }
        }
    }

    @Override
    public String detailAuthentication(Model model, HttpSession session, Long idAuthentication) {
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

                Authentication detailAuthentication = authenticationRepository.findById(idAuthentication).orElse(null);
                model.addAttribute("detailAuthentication", detailAuthentication);

                List<Role> itemsRole = roleRepository.getAllRole();
                model.addAttribute("listRole", itemsRole);

                model.addAttribute("checkMenuAdmin", true);
                return "admin/authentication/DetailAuthentication";
            }
        }
    }

    @Override
    public String updateAuthentication(Authentication updateAuthentication, Role roleSelect) {
        Authentication update = authenticationRepository.findById(updateAuthentication.getId()).orElse(null);
        update.setRole(roleSelect);
        authenticationRepository.save(update);
        return "redirect:/mangostore/admin/authentication";
    }

    @Override
    public ResponseEntity<String> checkUpdateAuthentication(IdAccountRequest request) {
        Integer countAdminBeforeUpdate = authenticationRepository.countAccountByRoleAdmin();
        boolean isCurrentAdmin = authenticationRepository.isAdminAccount(Long.parseLong(request.getIdAccount()));
        if (countAdminBeforeUpdate == 1 && isCurrentAdmin) {
            return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("0", HttpStatus.OK);
    }

}
