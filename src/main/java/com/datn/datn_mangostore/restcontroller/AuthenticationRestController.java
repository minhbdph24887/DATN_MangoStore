package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.request.IdAccountRequest;
import com.datn.datn_mangostore.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/mangostore/admin/authentication/")
public class AuthenticationRestController {
    private final AuthenticationService authenticationService;

    public AuthenticationRestController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "check-update")
    public ResponseEntity<String> checkUpdateAuthentication(@RequestBody IdAccountRequest request) {
        return authenticationService.checkUpdateAuthentication(request);
    }
}

