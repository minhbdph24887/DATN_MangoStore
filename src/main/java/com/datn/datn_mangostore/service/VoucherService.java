package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.bean.Voucher;
import com.datn.datn_mangostore.request.RestoreVoucherRequest;
import com.datn.datn_mangostore.request.VoucherRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface VoucherService {
    String indexVoucher(Model model,
                        HttpSession session,
                        String voucherActive);

    String addVoucher(Voucher addVoucher,
                      BindingResult result,
                      HttpSession session);

    String detailVoucher(Model model,
                         HttpSession session,
                         Long idVoucher);

    String updateVoucher(HttpSession session,
                         Voucher voucher);

    String deleteVoucher(Long idVoucher);

    boolean restoreVoucherAPI(RestoreVoucherRequest request);

    ResponseEntity<String> checkAddVoucher(VoucherRequest request);

    ResponseEntity<String> checkUpdateVoucher(VoucherRequest request);
}
