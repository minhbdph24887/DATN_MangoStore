package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Authentication;
import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.AuthenticationRepository;
import com.datn.datn_mangostore.repository.RoleRepository;
import com.datn.datn_mangostore.request.IdAuthenticationRequest;
import com.datn.datn_mangostore.service.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationRepository authenticationRepository;
    private final RoleRepository roleRepository;
    private final Gender gender;

    public AuthenticationServiceImpl(AuthenticationRepository authenticationRepository,
                                     RoleRepository roleRepository,
                                     Gender gender) {
        this.authenticationRepository = authenticationRepository;
        this.roleRepository = roleRepository;
        this.gender = gender;
    }

    @Override
    public String getAllAuthentication(Model model,
                                       HttpSession session) {
        Account detailAccount = gender.checkMenuAdmin(model, session, "Admin | Trang chủ xác thực");
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Authentication> itemsAuthentication = authenticationRepository.findAll();
            model.addAttribute("listAuthentication", itemsAuthentication);
            return "admin/authentication/IndexAuthentication";
        }
    }

    @Override
    public String detailAuthentication(Model model,
                                       HttpSession session,
                                       Long idAuthentication) {
        Account detailAccount = gender.checkMenuAdmin(model, session, "Admin | Cập nhật xác thực");
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            Authentication detailAuthentication = authenticationRepository.findById(idAuthentication).orElse(null);
            model.addAttribute("detailAuthentication", detailAuthentication);

            List<Role> itemsRole = roleRepository.getAllRole();
            model.addAttribute("listRole", itemsRole);
            return "admin/authentication/DetailAuthentication";
        }
    }

    @Override
    public String updateAuthentication(Authentication updateAuthentication,
                                       Role roleSelect) {
        Authentication update = authenticationRepository.findById(updateAuthentication.getId()).orElse(null);
        assert update != null;
        update.setRole(roleSelect);
        authenticationRepository.save(update);
        return "redirect:/mangostore/admin/authentication";
    }

    @Override
    public ResponseEntity<String> checkUpdateAuthentication(IdAuthenticationRequest request) {
        Authentication detailAuthentication = authenticationRepository.findById(Long.parseLong(request.getIdAuthentication())).orElse(null);
        Integer countAdminBeforeUpdate = authenticationRepository.countAccountByRoleAdmin();
        boolean isCurrentAdmin = authenticationRepository.isAdminAccount(Long.parseLong(request.getIdAccount()));
        assert detailAuthentication != null;
        if (detailAuthentication.getRole().getId() != Long.parseLong(request.getIdRole())) {
            if (countAdminBeforeUpdate == 1 && isCurrentAdmin) {
                return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("0", HttpStatus.OK);
    }
}