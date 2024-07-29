package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.request.AccountRequest;
import com.datn.datn_mangostore.request.DataRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    String getAllStaffByStatus1(Model model,
                                HttpSession session);

    String restoreStaff(Long idAccount);

    String restoreClient(Long idAccount);

    String detailStaff(Model model,
                       HttpSession session,
                       Long idAccount);

    String detailViewStaff(Model model,
                           HttpSession session);

    String detailclient(Model model,
                        HttpSession session,
                        Long idAccount);

    String updateStaff(BindingResult result,
                       String newPassword,
                       MultipartFile imageFile,
                       Account account,
                       HttpSession session);

    String updateClient(BindingResult result,
                        String newPassword,
                        MultipartFile imageFile,
                        Account account,
                        HttpSession session);

    String deleteStaff(Long idAccount);

    String deleteClient(Long idAccount);

    String addAccountClient(BindingResult result,
                            Account addClient);

    String addAccountStaff(BindingResult result,
                           Account addProfile);

    String getAllAccountByClient(Model model,
                                 HttpSession session);

    ResponseEntity<String> checkAddAccount(DataRequest request);

    ResponseEntity<String> checkUpdateAccount(DataRequest request);

    ResponseEntity<String> checkDeleteAccount(AccountRequest request);
}
