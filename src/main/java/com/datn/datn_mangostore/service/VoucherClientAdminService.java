package com.datn.datn_mangostore.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface VoucherClientAdminService {
    String indexVoucherClient(Model model, HttpSession session);


}
