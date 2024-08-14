package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.VoucherClient;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.VoucherClientRepository;
import com.datn.datn_mangostore.service.VoucherClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class VoucherClientServiceImpl implements VoucherClientService {
    private final VoucherClientRepository voucherClientRepository;
    private final Gender gender;

    public VoucherClientServiceImpl(VoucherClientRepository voucherClientRepository,
                                    Gender gender) {
        this.voucherClientRepository = voucherClientRepository;
        this.gender = gender;
    }

    @Override
    public String indexVoucherClient(Model model,
                                     HttpSession session) {
        Account detailAccount = gender.checkMenuAdmin(model, session, "Admin | Trang chủ phiếu giảm giá của khách hàng");
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<VoucherClient> itemsVoucherClient = voucherClientRepository.findAll();
            model.addAttribute("listVoucherClient", itemsVoucherClient);
            return "admin/voucherClient/IndexVoucherClient";
        }
    }

    @Override
    public String detailVoucherClient(Model model,
                                      HttpSession session,
                                      Long idVoucherClient) {
        Account detailAccount = gender.checkMenuAdmin(model, session, "Admin | Phiếu giảm giá của khách hàng");
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            VoucherClient detailVoucher = voucherClientRepository.findById(idVoucherClient).orElse(null);
            model.addAttribute("voucherClient", detailVoucher);
            return "admin/voucherClient/DetailVoucherClient";
        }
    }
}