package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.RoleRepository;
import com.datn.datn_mangostore.request.RoleRequest;
import com.datn.datn_mangostore.service.RoleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public RoleServiceImpl(AccountRepository accountRepository, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public String getAllRoleByStatus1(Model model, HttpSession session) {
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

                List<Role> itemsRole = roleRepository.getAllRole();
                model.addAttribute("listRole", itemsRole);

                List<Role> itemsRoleInactive = roleRepository.getAllRoleByStatus0();
                model.addAttribute("listRoleInactive", itemsRoleInactive);


                model.addAttribute("addRole", new Role());

                model.addAttribute("checkMenuAdmin", true);
                return "admin/role/IndexRole";
            }
        }
    }

    @Override
    public String restoreRole(Long idRole) {

        Role detailRole = roleRepository.findById(idRole).orElse(null);
        detailRole.setStatus(1);
        roleRepository.save(detailRole);
        return "redirect:/mangostore/admin/role";
    }

    @Override
    public String detailRole(Model model, HttpSession session, Long idRole) {
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

                List<Role> itemsRoleInactive = roleRepository.getAllRoleByStatus0();
                model.addAttribute("listRoleInactive", itemsRoleInactive);

                Role detailRole = roleRepository.findById(idRole).orElse(null);
                model.addAttribute("detailRole", detailRole);

                List<Account> itemsAccount = accountRepository.getAllAccountByRole(idRole);
                model.addAttribute("listAccountByRole", itemsAccount);

                model.addAttribute("checkMenuAdmin", true);
                return "admin/role/DetailRole";
            }
        }
    }

    @Override
    public String updateRole(BindingResult result, Long idRole, Role role) {
        Role detailRole = roleRepository.findById(role.getId()).orElse(null);
        assert detailRole != null;
        detailRole.setNote(role.getNote());

        roleRepository.save(detailRole);
        return "redirect:/mangostore/admin/role";
    }

    @Override
    public String deleteRole(Long idRole) {
        Role detaiRole = roleRepository.findById(idRole).orElse(null);
        assert detaiRole != null;
        detaiRole.setStatus(0);
        roleRepository.save(detaiRole);
        return "redirect:/mangostore/admin/role";
    }

    @Override
    public String addRole(BindingResult result, Role addRole) {
        Role newRole = new Role();
        newRole.setName(addRole.getName());
        newRole.setNote(addRole.getNote());
        newRole.setStatus(1);
        roleRepository.save(newRole);
        return "redirect:/mangostore/admin/role";
    }

    @Override
    public ResponseEntity<String> checkAddRole(RoleRequest request) {
        Role detailRoleCodeRole = roleRepository.findRoleByCodeRole(request.getCodeRole());
        if (detailRoleCodeRole != null) {
            return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("0", HttpStatus.OK);

        }
    }
}
