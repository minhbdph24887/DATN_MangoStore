package com.datn.datn_mangostore.controller;

import com.datn.datn_mangostore.bean.AddressClient;
import com.datn.datn_mangostore.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/mangostore/")
public class CartClientController {
    private final CartService cartService;

    public CartClientController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping(value = "cart")
    public String indexCart(Model model,
                            HttpSession session) {
        return cartService.indexCart(model, session);
    }

    @PostMapping(value = "cart/reduce")
    public String reduceProductCart(@RequestParam("id") Long idShoppingCartDetail) {
        return cartService.reduceQuantity(idShoppingCartDetail);
    }

    @PostMapping(value = "cart/increase")
    public String increaseProductCart(@RequestParam("id") Long idShoppingCartDetail) {
        return cartService.increaseQuantity(idShoppingCartDetail);
    }

    @GetMapping(value = "cart/delete")
    public String deleteProductCart(@RequestParam("id") Long idShoppingCartDetail) {
        return cartService.deleteProductCart(idShoppingCartDetail);
    }

    @GetMapping(value = "cart/checkout")
    public String viewCheckOut(Model model,
                               HttpSession session) {
        return cartService.viewCheckOut(model, session);
    }

    @PostMapping(value = "cart/address-client/create")
    public String addAddressForClient(@Valid AddressClient newAddressClient,
                                      HttpSession session) {
        return cartService.addAddressClientForClient(newAddressClient, session);
    }

    @PostMapping(value = "cart/client-address/update-status")
    public String updateStatusDefaultClientAddress(@RequestParam("id") Long id,
                                                   HttpSession session) {
        return cartService.updateStatusClientAddress(id, session);
    }

    @PostMapping(value = "cart/address-client/update")
    public String updateAddressClientForCheckOut(@Valid AddressClient editAddressClient) {
        return cartService.updateAddressClientForCheckOut(editAddressClient);
    }

    @PostMapping(value = "cart/client-point/add")
    public String addPointClientToInvoice(HttpSession session) {
        return cartService.addPointClientToInvoice(session);
    }

    @PostMapping(value = "cart/client-voucher/add")
    public String addVoucherToInvoice(@RequestParam("idVoucher") Long id,
                                      HttpSession session) {
        return cartService.addVoucherToInvoice(id, session);
    }

    @GetMapping(value = "cart/banking-status")
    public String viewBankingStatus(@RequestParam("id") Long idInvoice,
                                    HttpServletRequest request,
                                    HttpSession session) {
        return cartService.bankingVnPay(idInvoice, request, session);
    }
}