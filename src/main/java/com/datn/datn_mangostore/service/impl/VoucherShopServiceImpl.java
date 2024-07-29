package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Voucher;
import com.datn.datn_mangostore.bean.VoucherClient;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.VoucherClientRepository;
import com.datn.datn_mangostore.repository.VoucherRepository;
import com.datn.datn_mangostore.request.VoucherShopClientRequest;
import com.datn.datn_mangostore.service.VoucherShopService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class VoucherShopServiceImpl implements VoucherShopService {
    private final AccountRepository accountRepository;
    private final VoucherRepository voucherRepository;
    private final VoucherClientRepository voucherClientRepository;
    private final Gender gender;

    public VoucherShopServiceImpl(AccountRepository accountRepository,
                                  VoucherRepository voucherRepository,
                                  VoucherClientRepository voucherClientRepository,
                                  Gender gender) {
        this.accountRepository = accountRepository;
        this.voucherRepository = voucherRepository;
        this.voucherClientRepository = voucherClientRepository;
        this.gender = gender;
    }

    @Override
    public String indexVoucherShop(Model model,
                                   HttpSession session) {
        Account detailAccount = gender.checkMenuClient(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Voucher> getAllVoucher = voucherRepository.getAllVoucherOnline();
            model.addAttribute("listVoucher", getAllVoucher);
            return "client/VoucherShop/IndexVoucherShopClient";
        }
    }

    @Override
    public boolean addVoucherClient(VoucherShopClientRequest request,
                                    HttpSession session) {
        Voucher voucher = voucherRepository.findById(request.getIdVoucher()).orElse(null);
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        assert voucher != null;
        VoucherClient existingVoucherClient = voucherClientRepository.findVoucherClientByAccountAndVoucher(detailAccount.getId(), voucher.getId());
        if (existingVoucherClient != null) {
            return false;
        } else {
            VoucherClient voucherClient = new VoucherClient();
            voucherClient.setCodeVoucher(gender.generateCode());
            voucherClient.setNameVoucher(voucher.getNameVoucher());
            voucherClient.setReducedValue(voucher.getReducedValue());
            voucherClient.setMinimumPrice(voucher.getMinimumPrice());
            voucherClient.setAccount(detailAccount);
            voucherClient.setVoucher(voucher);
            voucherClient.setSaveDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            voucherClient.setStartDay(voucher.getStartDay());
            voucherClient.setEndDate(voucher.getEndDate());
            voucherClient.setVoucherStatus(voucher.getVoucherStatus());
            voucherClient.setStatus(voucher.getStatus());
            voucherClientRepository.save(voucherClient);

            int newQuantityVoucher = voucher.getQuantity() - 1;
            if (newQuantityVoucher == 0) {
                voucher.setQuantity(0);
                voucher.setStatus(0);
                voucherRepository.save(voucher);
            } else {
                voucher.setQuantity(newQuantityVoucher);
                voucherRepository.save(voucher);
            }
            return true;
        }
    }
}