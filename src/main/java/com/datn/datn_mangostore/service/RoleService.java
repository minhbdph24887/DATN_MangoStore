package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.request.RoleRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface RoleService {
    String getAllRoleByStatus1(Model model,
                               HttpSession session);

    String restoreRole(Long idRole);

    String detailRole(Model model,
                      HttpSession session,
                      Long idRole);

    String updateRole(BindingResult result,
                      Long idRole,
                      Role role);

    String deleteRole(Long idRole);

    String addRole(BindingResult result,
                   Role addRole);

    ResponseEntity<String> checkAddRole(RoleRequest request);
}
