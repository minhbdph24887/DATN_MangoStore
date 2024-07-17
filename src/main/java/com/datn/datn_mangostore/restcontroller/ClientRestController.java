package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.request.ProfileRequest;
import com.datn.datn_mangostore.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/mangostore/")
public class ClientRestController {
    private final ClientService clientService;

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping(value = "profile/check-update")
    public ResponseEntity<String> checkUpdateProfile(@RequestBody ProfileRequest request){
        return clientService.checkUpdateProfile(request);
    }
}
