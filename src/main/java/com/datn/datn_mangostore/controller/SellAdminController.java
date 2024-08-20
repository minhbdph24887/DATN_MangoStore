package com.datn.datn_mangostore.controller;

import com.datn.datn_mangostore.service.SellService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/mangostore/admin/")
public class SellAdminController {
    private final SellService sellService;

    public SellAdminController(SellService sellService) {
        this.sellService = sellService;
    }

    @GetMapping(value = "sell")
    public String indexSellOffline(Model model,
                                   HttpSession session) {
        return sellService.indexSellAdmin(model,
                session);
    }

    @GetMapping(value = "sell/edit")
    public String editInvoice(@RequestParam("id") Long idInvoice,
                              Model model,
                              HttpSession session) {
        return sellService.editInvoice(idInvoice,
                model,
                session);
    }

    @PostMapping(value = "sell/update-point")
    public String updateInvoicePoint(@RequestParam("id") Long idInvoice,
                                     @RequestParam("point") Integer pointClient) {
        return sellService.updatePoint(idInvoice,
                pointClient);
    }

    @PostMapping(value = "sell/delete")
    public String cancelInvoice(@RequestParam("id") Long idInvoice) {
        return sellService.cancelInvoice(idInvoice);
    }

    @PostMapping(value = "sell/delete-product")
    public String deleteProduct(@RequestParam("id") Long idInvoiceDetail) {
        return sellService.deleteProduct(idInvoiceDetail);
    }

    @GetMapping(value = "sell/banking")
    public String bankingVnPay(@RequestParam("id") Long idInvoice,
                               HttpServletRequest request,
                               HttpSession session) {
        return sellService.paymentVnPay(idInvoice,
                request,
                session);
    }
}
