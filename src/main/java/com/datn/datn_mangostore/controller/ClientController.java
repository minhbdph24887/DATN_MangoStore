package com.datn.datn_mangostore.controller;

import com.datn.datn_mangostore.service.ClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
