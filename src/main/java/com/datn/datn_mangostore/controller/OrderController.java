package com.datn.datn_mangostore.controller;

import com.datn.datn_mangostore.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/mangostore/admin/")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "order/show")
    public String orderShow(Model model,
                            HttpSession session) {
        return orderService.orderShow(model, session);
    }

    @GetMapping(value = "confirm-invoice")
    public String confirmInvoice(@RequestParam("id") Long idInvoice,
                                 HttpSession session) {
        return orderService.confirmInvoice(idInvoice, session);
    }

    @GetMapping(value = "order/list")
    public String listInvoice(Model model,
                              HttpSession session) {
        return orderService.listInvoice(model, session);
    }

    @GetMapping(value = "order/detail/{id}")
    public String detailOrderAdmin(@PathVariable("id") Long idInvoice,
                                   Model model,
                                   HttpSession session) {
        return orderService.detailOrderAdmin(idInvoice, model, session);
    }

    @GetMapping(value = "order/update-invoice-status/{idInvoice}")
    public String updateInvoiceStatus(@PathVariable("idInvoice") Long idInvoice) {
        return orderService.updateInvoiceStatusAdmin(idInvoice);
    }
}
