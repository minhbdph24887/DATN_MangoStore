package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.bean.Origin;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface OriginService {
    String indexOrigin(Model model,
                       HttpSession session,
                       String keyword);

    String addOrigin(Origin addOrigin,
                     BindingResult result,
                     HttpSession session);

    String detailOrigin(Model model,
                        HttpSession session,
                        Long idOrigin);

    String updateOrigin(BindingResult result,
                        HttpSession session,
                        Origin origin);

    String deleteOrigin(Long idOrigin);

    String restoreOrigin(Long idOrigin);


    Integer findByOriginCreateExit(String name);

    Integer findByOriginUpdateExit(String codeOrigin,
                                   String name);
}
