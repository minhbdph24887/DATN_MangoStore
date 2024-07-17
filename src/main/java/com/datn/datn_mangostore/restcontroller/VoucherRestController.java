package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.request.RestoreVoucherRequest;
import com.datn.datn_mangostore.request.VoucherRequest;
import com.datn.datn_mangostore.service.VoucherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/mangostore/admin/voucher/")
public class VoucherRestController {
    private final VoucherService voucherService;

    public VoucherRestController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @PostMapping(value = "check-add")
    public ResponseEntity<String> checkAddVoucher(@RequestBody VoucherRequest request){
        return voucherService.checkAddVoucher(request);
    }

    @PostMapping(value = "check-update")
    public ResponseEntity<String> checkUpdateVoucher(@RequestBody VoucherRequest request){
        return voucherService.checkUpdateVoucher(request);
    }

    @PostMapping(value = "restore")
    public boolean restoreVoucher(@RequestBody RestoreVoucherRequest request) {
        return voucherService.restoreVoucherAPI(request);
    }
}
