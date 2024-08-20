package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.request.VoucherShopClientRequest;
import com.datn.datn_mangostore.service.VoucherShopService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/mangostore/admin/voucher")
public class VoucherClientRestController {
    private final VoucherShopService voucherShopService;

    public VoucherClientRestController(VoucherShopService voucherShopService) {
        this.voucherShopService = voucherShopService;
    }

    @PostMapping(value = "add-client")
    public boolean addVoucherClient(@RequestBody VoucherShopClientRequest request,
                                    HttpSession session) {
        return voucherShopService.addVoucherClient(request,
                session);
    }
}