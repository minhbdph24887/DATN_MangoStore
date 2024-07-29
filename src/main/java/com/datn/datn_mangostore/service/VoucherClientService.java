package com.datn.datn_mangostore.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface VoucherClientService {
    String indexVoucherClient(Model model,
                              HttpSession session);

    String detailVoucherClient(Model model,
                               HttpSession session,
                               Long idVoucherClient);
}
