package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.request.AccountRequest;
import com.datn.datn_mangostore.request.DataRequest;
import com.datn.datn_mangostore.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/mangostore/admin/account/")
public class AccountRestController {
    private final ProfileService profileService;

    public AccountRestController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping(value = "check-add")
    public ResponseEntity<String> checkAddAccount(@RequestBody DataRequest request) {
        return profileService.checkAddAccount(request);
    }

    @PostMapping(value = "check-update")
    public ResponseEntity<String> checkUpdateAccount(@RequestBody DataRequest request) {
        return profileService.checkUpdateAccount(request);
    }

    @PostMapping(value = "check-delete")
    public ResponseEntity<String> checkDeleteAccount(@RequestBody AccountRequest request) {
        return profileService.checkDeleteAccount(request);
    }
}
