package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.bean.Authentication;
import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.request.IdAuthenticationRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface AuthenticationService {

    String getAllRole(Model model,
                      HttpSession session);

    String detailAuthentication(Model model,
                                HttpSession session,
                                Long idAuthentication);

    String updateAuthentication(Authentication updateAuthentication,
                                Role roleSelect);

    ResponseEntity<String> checkUpdateAuthentication(IdAuthenticationRequest request);
}
