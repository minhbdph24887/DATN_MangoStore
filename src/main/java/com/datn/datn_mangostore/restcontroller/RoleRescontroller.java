package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.request.DataRequest;
import com.datn.datn_mangostore.request.RoleRequest;
import com.datn.datn_mangostore.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/mangostore/admin/role/")
public class RoleRescontroller {
    private final RoleService roleService;

    public RoleRescontroller(RoleService roleService) {
        this.roleService = roleService;
    }
    @PostMapping(value = "check-role")
    public ResponseEntity<String> checkAddRole(@RequestBody RoleRequest request){
        return roleService.checkAddRole(request);
    }
}
