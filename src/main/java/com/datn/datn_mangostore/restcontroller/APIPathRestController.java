package com.datn.datn_mangostore.restcontroller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/")
public class APIPathRestController {
    @GetMapping(value = "address", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource getAddressData() {
        return new ClassPathResource("static/json/api-address.json");
    }
}
