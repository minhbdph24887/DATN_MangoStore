package com.datn.datn_mangostore.controller;

import com.datn.datn_mangostore.service.VoucherShopService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/mangostore/")
public class VoucherShopController {
    private final VoucherShopService voucherShopService;

    public VoucherShopController(VoucherShopService voucherShopService) {
        this.voucherShopService = voucherShopService;
    }

    @GetMapping(value = "voucher/shop")
    public String indexVoucherShop(Model model,
                                   HttpSession session) {
        return voucherShopService.indexVoucherShop(model, session);
    }
}
