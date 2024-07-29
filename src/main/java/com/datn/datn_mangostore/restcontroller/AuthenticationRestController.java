package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.request.IdAuthenticationRequest;
import com.datn.datn_mangostore.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/mangostore/admin/authentication/")
public class AuthenticationRestController {
    private final AuthenticationService authenticationService;

    public AuthenticationRestController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "check-update")
    public ResponseEntity<String> checkUpdateAuthentication(@RequestBody IdAuthenticationRequest request) {
        return authenticationService.checkUpdateAuthentication(request);
    }
}

