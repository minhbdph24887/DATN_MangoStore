package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.bean.Voucher;
import com.datn.datn_mangostore.request.RestoreVoucherRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface VoucherService {
    String indexVoucher(Model model,
                        HttpSession session,
                        String keyword);

    String addVoucher(Voucher addVoucher,
                      BindingResult result, RedirectAttributes redirectAttributes,
                      HttpSession session, Model model);

    String detailVoucher(Model model,
                         HttpSession session,
                         Long idVoucher);

    String updateVoucher( RedirectAttributes redirectAttributes,
                          HttpSession session,
                          Voucher voucher, Model model, String reducedValueValidate, String minimumPriceValidate);

    String deleteVoucher(RedirectAttributes redirectAttributes,Long idVoucher);

    String searchVoucher(Model model, HttpSession session,String keyword);

    String searchVoucherInactive(Model model, HttpSession session,String keyword);

    boolean restoreVoucherAPI(RestoreVoucherRequest request);

}
