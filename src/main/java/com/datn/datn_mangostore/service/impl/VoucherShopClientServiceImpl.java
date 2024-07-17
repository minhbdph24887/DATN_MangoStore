package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.bean.Voucher;
import com.datn.datn_mangostore.bean.VoucherClient;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.RoleRepository;
import com.datn.datn_mangostore.repository.VoucherClientRepository;
import com.datn.datn_mangostore.repository.VoucherRepository;
import com.datn.datn_mangostore.request.VoucherShopClientRequest;
import com.datn.datn_mangostore.service.VoucherShopClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class VoucherShopClientServiceImpl implements VoucherShopClientService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final VoucherRepository voucherRepository;
    private final Gender gender;
    private final VoucherClientRepository voucherClientRepository;

    public VoucherShopClientServiceImpl(AccountRepository accountRepository,
                                        RoleRepository roleRepository,
                                        VoucherRepository voucherRepository,
                                        Gender gender,
                                        VoucherClientRepository voucherClientRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.voucherRepository = voucherRepository;
        this.gender = gender;
        this.voucherClientRepository = voucherClientRepository;
    }

    @Override
    public String indexVoucherShop(Model model, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email != null) {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);
            Role detailRoleByEmail = roleRepository.getRoleByEmail(email);
            if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                model.addAttribute("checkAuthentication", detailRoleByEmail);
            }
        }

        List<Voucher> getAllVoucher = voucherRepository.getAllVoucherOnline();
        model.addAttribute("listVoucher", getAllVoucher);
        return "client/VoucherShop";
    }

    @Override
    public boolean addVoucherClient(VoucherShopClientRequest request, HttpSession session) {
        Voucher voucher = voucherRepository.findById(request.getIdVoucher()).orElse(null);
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        assert voucher != null;
        VoucherClient existingVoucherClient = voucherClientRepository.voucherClientByAccountAndVoucher(detailAccount.getId(), voucher.getId());
        if (existingVoucherClient != null) {
            return false;
        } else {
            VoucherClient voucherClient = new VoucherClient();
            voucherClient.setCodeVoucher(gender.generateCode());
            voucherClient.setNameVoucher(voucher.getNameVoucher());
            voucherClient.setReducedValue(voucher.getReducedValue());
            voucherClient.setAccount(detailAccount);
            voucherClient.setVoucher(voucher);
            voucherClient.setSaveDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            voucherClient.setStartDay(voucher.getStartDay());
            voucherClient.setEndDate(voucher.getEndDate());
            voucherClient.setVoucherStatus(voucher.getVoucherStatus());
            voucherClient.setStatus(voucher.getStatus());
            voucherClientRepository.save(voucherClient);

            int newQuantity = voucher.getQuantity() - 1;
            if (newQuantity == 0) {
                voucher.setQuantity(0);
                voucher.setStatus(0);
                voucherRepository.save(voucher);
            } else {
                voucher.setQuantity(newQuantity);
                voucherRepository.save(voucher);
            }
            return true;
        }
    }
}
