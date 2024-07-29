package com.datn.datn_mangostore.controller;

import com.datn.datn_mangostore.bean.Rank;
import com.datn.datn_mangostore.bean.Voucher;
import com.datn.datn_mangostore.service.RankService;
import com.datn.datn_mangostore.service.VoucherClientService;
import com.datn.datn_mangostore.service.VoucherService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/mangostore/admin/")
public class VoucherController {
    private final RankService rankService;
    private final VoucherService voucherService;
    private final VoucherClientService voucherClientService;

    public VoucherController(RankService rankService,
                             VoucherService voucherService,
                             VoucherClientService voucherClientService) {
        this.rankService = rankService;
        this.voucherService = voucherService;
        this.voucherClientService = voucherClientService;
    }

    @GetMapping(value = "rank")
    public String indexRank(Model model,
                            HttpSession session,
                            @Param("keyword") String keyword) {
        return rankService.indexRank(model, session, keyword);
    }

    @PostMapping(value = "rank/add")
    public String addRank(@Valid Rank addRank,
                          BindingResult result,
                          HttpSession session) {
        return rankService.addRank(addRank, result, session);
    }

    @GetMapping(value = "rank/detail/{id}")
    public String detailRank(Model model,
                             HttpSession session,
                             @PathVariable("id") Long idRank) {
        return rankService.detailRank(model, session, idRank);
    }

    @PostMapping(value = "rank/update")
    public String updateRank(@Valid Rank rank,
                             HttpSession session) {
        return rankService.updateRank(session, rank);
    }

    @GetMapping(value = "rank/delete/{id}")
    public String deleteRank(@PathVariable("id") Long idRank) {
        return rankService.deleteRank(idRank);
    }

    @GetMapping(value = "rank/restore/{id}")
    public String restoreRank(@PathVariable("id") Long idRank) {
        return rankService.restoreRank(idRank);
    }


    @GetMapping(value = "voucher")
    public String indexVoucher(Model model,
                               HttpSession session,
                               @Param("voucherActive") String voucherActive) {
        return voucherService.indexVoucher(model, session, voucherActive);
    }

    @PostMapping(value = "voucher/add")
    public String addVoucher(@Valid Voucher addVoucher,
                             BindingResult result,
                             HttpSession session) {
        return voucherService.addVoucher(addVoucher, result, session);
    }

    @GetMapping(value = "voucher/detail/{id}")
    public String detailVoucher(Model model,
                                HttpSession session,
                                @PathVariable("id") Long idVoucher) {
        return voucherService.detailVoucher(model, session, idVoucher);
    }


    @PostMapping(value = "voucher/update/{id}")
    public String updateVoucher(@Valid Voucher voucher,
                                HttpSession session) {
        return voucherService.updateVoucher(session, voucher);
    }

    @GetMapping(value = "voucher/delete/{id}")
    public String deleteVoucher(@PathVariable("id") Long idVoucher) {
        return voucherService.deleteVoucher(idVoucher);
    }


    @GetMapping(value = "voucher-client")
    public String detailVoucherClient(Model model,
                                      HttpSession session) {
        return voucherClientService.indexVoucherClient(model, session);
    }

    @GetMapping(value = "voucher-client/detail/{id}")
    public String detailVoucherClient(Model model,
                                      HttpSession session,
                                      @PathVariable("id") Long idVoucher) {
        return voucherClientService.detailVoucherClient(model, session, idVoucher);
    }
}
