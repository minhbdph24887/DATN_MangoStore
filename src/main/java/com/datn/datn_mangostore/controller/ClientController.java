package com.datn.datn_mangostore.controller;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.service.ClientService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/mangostore/")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
        ;
    }

    @GetMapping(value = "home")
    public String indexClient(Model model,
                              HttpSession session) {
        return clientService.indexClient(model, session);
    }

    @GetMapping(value = "profile")
    public String viewProfileClient(Model model,
                                    HttpSession session) {
        return clientService.indexProfile(model, session);
    }

    @PostMapping(value = "profile/update")
    public String updateProfileClient(@Valid Account profile,
                                      @RequestParam("imageFile") MultipartFile imageFile,
                                      HttpSession session) {
        return clientService.updateProfile(profile, imageFile, session);
    }

    @GetMapping(value = "change-password")
    public String viewChangePassword(Model model,
                                     HttpSession session) {
        return clientService.changePassword(model, session);
    }

    @PostMapping(value = "change-password/update")
    public String changePassword(@Valid Account profile,
                                 HttpSession session) {
        return clientService.updateChangePassword(profile, session);
    }

    @GetMapping(value = "voucher-wallet")
    public String viewVoucherClient(HttpSession session,
                                    Model model) {
        return clientService.indexVoucherClient(model, session);
    }

    @GetMapping(value = "purchase")
    public String viewPurchase(Model model,
                               HttpSession session,
                               @RequestParam(name = "status", defaultValue = "all") String status,
                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                               @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
        return clientService.indexViewPurchase(model, session, status, pageNo, pageSize);
    }

    @GetMapping(value = "purchase/remove")
    public String cancelPurchase(@RequestParam("id") Long idInvoice) {
        return clientService.cancelPurchase(idInvoice);
    }
}