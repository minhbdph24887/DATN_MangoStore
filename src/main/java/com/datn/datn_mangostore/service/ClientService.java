package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.request.ProfileRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface ClientService {
    String indexClient(Model model,
                       HttpSession session);

    String indexProfile(Model model,
                        HttpSession session);

    ResponseEntity<String> checkUpdateProfile(ProfileRequest request);

    String updateProfile(Account profile,
                         MultipartFile imageFile,
                         HttpSession session);

    String changePassword(Model model,
                          HttpSession session);

    String updateChangePassword(Account profile,
                                HttpSession session);

    String indexVoucherClient(Model model,
                              HttpSession session);

    String indexViewPurchase(Model model,
                             HttpSession session,
                             String status,
                             Integer pageNo,
                             Integer pageSize);

    String cancelPurchase(Long idInvoice);
}