package com.datn.datn_mangostore.controller;

import com.datn.datn_mangostore.bean.Rank;
import com.datn.datn_mangostore.bean.Voucher;
import com.datn.datn_mangostore.repository.VoucherRepository;
import com.datn.datn_mangostore.service.RankService;
import com.datn.datn_mangostore.service.VoucherClientService;
import com.datn.datn_mangostore.service.VoucherService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequestMapping(value = "/mangostore/admin/")
public class VoucherController {
    private final RankService rankService;
    private final VoucherService voucherService;
    private final VoucherClientService voucherClientService;
    private final VoucherRepository voucherRepository;
    public VoucherController(RankService rankService,
                             VoucherService voucherService,
                             VoucherClientService voucherClientService,
                             VoucherRepository voucherRepository) {
        this.rankService = rankService;
        this.voucherService = voucherService;
        this.voucherRepository = voucherRepository;
        this.voucherClientService = voucherClientService;
    }

    @GetMapping(value = "rank")
    public String indexRank(Model model,
                            HttpSession session,
                            @Param("keyword") String keyword) {
        return rankService.indexRank(model, session, keyword);
    }

    @PostMapping(value = "rank/add")
    public String addRank(@Valid Rank addRank, RedirectAttributes redirectAttributes,
                          BindingResult result,
                          HttpSession session, Model model) {
        return rankService.addRank(addRank, result, session, model, redirectAttributes);
    }

    @GetMapping(value = "rank/detail/{id}")
    public String detailRank(Model model, RedirectAttributes redirectAttributes,
                             HttpSession session,
                             @PathVariable("id") Long idRank) {
        return rankService.detailRank(model, redirectAttributes, session, idRank);
    }

    @PostMapping(value = "rank/search")
    public String searchRank(Model model,
                             HttpSession session,
                             @Param("keyword") String keyword) {
        return rankService.searchRank(model, session, keyword);
    }

    @PostMapping(value = "voucher/search")
    public String searchVoucher(Model model,
                                HttpSession session,
                                @Param("keyword") String keyword) {
        return voucherService.searchVoucher(model, session, keyword);
    }

    @PostMapping(value = "voucher/search/in-active")
    public String searchInactiveVoucher(Model model,
                                        HttpSession session,
                                        @Param("keyword") String keyword) {
        return voucherService.searchVoucherInactive(model, session, keyword);
    }

    @PostMapping(value = "rank/update")
    public String updateRank(@Valid Rank rank, HttpSession session
            , Model model, RedirectAttributes redirectAttributes) {
        return rankService.updateRank(session, rank, model, redirectAttributes);
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
                               @Param("keyword") String keyword) {
        return voucherService.indexVoucher(model, session, keyword);
    }

    @PostMapping(value = "voucher/add")
    public String addVoucher(@Valid Voucher addVoucher, @Param("minimumPriceValidate") String minimumPriceValidate,
                             @Param("reducedValueValidate") String reducedValueValidate,
                             BindingResult result, RedirectAttributes redirectAttributes,
                             HttpSession session, Model model) {
        if (minimumPriceValidate != null && !minimumPriceValidate.isEmpty()) {
            Integer minimumPrice = Integer.parseInt(minimumPriceValidate.replaceAll("\\.", ""));
            addVoucher.setMinimumPrice(minimumPrice);
        }
        if (reducedValueValidate != null && !reducedValueValidate.isEmpty()) {
            Integer reducedValue = Integer.parseInt(reducedValueValidate.replaceAll("\\.", ""));
            addVoucher.setReducedValue(reducedValue);
        }
        return voucherService.addVoucher(addVoucher, result, redirectAttributes, session, model);
    }

    @GetMapping(value = "voucher/detail/{id}")
    public String detailVoucher(Model model,
                                HttpSession session,
                                @PathVariable("id") Long idVoucher) {
        return voucherService.detailVoucher(model, session, idVoucher);
    }


    @PostMapping(value = "voucher/update/{id}")
    public String updateVoucher(@Valid Voucher voucher, @RequestParam("minimumOrderInput") String minimumPriceValidate,
                                @RequestParam("reducedVoucherInput") String reducedValueValidate,
                                RedirectAttributes redirectAttributes, @PathVariable("id") Long idVoucher,
                                HttpSession session, Model model) {
        return voucherService.updateVoucher(redirectAttributes, session, voucher, model,
                reducedValueValidate,  minimumPriceValidate);
    }

    @GetMapping(value = "voucher/delete/{id}")
    public String deleteVoucher(@PathVariable("id") Long idVoucher, RedirectAttributes redirectAttributes) {
        return voucherService.deleteVoucher(redirectAttributes, idVoucher);
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
