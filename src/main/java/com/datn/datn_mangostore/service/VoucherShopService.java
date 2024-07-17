package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.request.VoucherShopClientRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface VoucherShopService {
    String indexVoucherShop(Model model,
                            HttpSession session);

    boolean addVoucherClient(VoucherShopClientRequest request,
                             HttpSession session);
}