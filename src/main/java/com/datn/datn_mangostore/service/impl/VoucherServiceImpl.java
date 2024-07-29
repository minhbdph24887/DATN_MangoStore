package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Rank;
import com.datn.datn_mangostore.bean.Voucher;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.RankRepository;
import com.datn.datn_mangostore.repository.VoucherRepository;
import com.datn.datn_mangostore.request.RestoreVoucherRequest;
import com.datn.datn_mangostore.request.VoucherRequest;
import com.datn.datn_mangostore.service.VoucherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class VoucherServiceImpl implements VoucherService {
    private final AccountRepository accountRepository;
    private final VoucherRepository voucherRepository;
    private final RankRepository rankRepository;
    private final Gender gender;
    private static final String regex = "^(?=.*[A-Z])(?=.*\\d).{10}$";

    public VoucherServiceImpl(AccountRepository accountRepository,
                              VoucherRepository voucherRepository,
                              RankRepository rankRepository,
                              Gender gender) {
        this.accountRepository = accountRepository;

        this.voucherRepository = voucherRepository;
        this.rankRepository = rankRepository;
        this.gender = gender;
    }

    @Override
    public String indexVoucher(Model model,
                               HttpSession session,
                               String voucherActive) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Voucher> itemsVoucher = voucherRepository.getAllVoucherByStatus1And2();
            if (voucherActive != null) {
                if (voucherActive.matches(regex)) {
                    itemsVoucher = voucherRepository.searchVoucherByCode(voucherActive, 1);
                } else {
                    itemsVoucher = voucherRepository.searchVoucherByName(voucherActive, 1);
                }
                model.addAttribute("voucherActive", voucherActive);
            }
            model.addAttribute("listVoucher", itemsVoucher);

            List<Voucher> itemsVoucherInactive = voucherRepository.getAllVoucherByStatus0();
            model.addAttribute("listVoucherInactive", itemsVoucherInactive);

            model.addAttribute("addVoucher", new Voucher());

            List<Rank> itemsRank = rankRepository.getAllRankByStatus1();
            model.addAttribute("listRank", itemsRank);
            return "admin/voucher/IndexVoucher";
        }
    }

    @Override
    public String addVoucher(Voucher addVoucher,
                             BindingResult result,
                             HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);

        Voucher newVoucher = new Voucher();
        if (addVoucher.getCodeVoucher().isEmpty()) {
            newVoucher.setCodeVoucher(gender.generateCode());
        } else {
            newVoucher.setCodeVoucher(addVoucher.getCodeVoucher());
        }
        newVoucher.setNameVoucher(addVoucher.getNameVoucher());
        newVoucher.setQuantity(addVoucher.getQuantity());
        newVoucher.setReducedValue(addVoucher.getReducedValue());
        newVoucher.setMinimumPrice(addVoucher.getMinimumPrice());
        newVoucher.setVoucherFrom(addVoucher.getVoucherFrom());
        newVoucher.setRank(addVoucher.getRank());
        newVoucher.setStartDay(addVoucher.getStartDay());
        newVoucher.setEndDate(addVoucher.getEndDate());
        newVoucher.setUserCreate(detailAccount.getFullName());
        newVoucher.setUserUpdate(detailAccount.getFullName());
        newVoucher.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newVoucher.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));

        LocalDate now = LocalDate.now();
        if ((addVoucher.getStartDay().isBefore(now) || addVoucher.getStartDay().isEqual(now)) && (addVoucher.getEndDate().isEqual(now) || addVoucher.getEndDate().isAfter(now))) {
            newVoucher.setVoucherStatus(1);
        } else if (addVoucher.getStartDay().isAfter(now) && addVoucher.getEndDate().isAfter(now)) {
            newVoucher.setVoucherStatus(2);
        }

        newVoucher.setStatus(1);
        voucherRepository.save(newVoucher);
        return "redirect:/mangostore/admin/voucher";
    }


    @Override
    public String detailVoucher(Model model,
                                HttpSession session,
                                Long idVoucher) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Voucher> itemsVoucherInactive = voucherRepository.getAllVoucherByStatus0();
            model.addAttribute("listVoucherInactive", itemsVoucherInactive);

            List<Rank> itemsRank = rankRepository.getAllRankByStatus1();
            model.addAttribute("listRank", itemsRank);

            Voucher detailVoucher = voucherRepository.findById(idVoucher).orElse(null);
            model.addAttribute("detailVoucher", detailVoucher);
            return "admin/voucher/DetailVoucher";
        }
    }

    @Override
    public String updateVoucher(HttpSession session,
                                Voucher voucher) {
        Voucher detailVoucher = voucherRepository.findById(voucher.getId()).orElse(null);
        assert detailVoucher != null;
        detailVoucher.setNameVoucher(voucher.getNameVoucher());
        detailVoucher.setQuantity(voucher.getQuantity());
        detailVoucher.setReducedValue(voucher.getReducedValue());
        detailVoucher.setVoucherFrom(voucher.getVoucherFrom());
        detailVoucher.setRank(voucher.getRank());
        detailVoucher.setStartDay(voucher.getStartDay());
        detailVoucher.setEndDate(voucher.getEndDate());

        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        detailVoucher.setUserUpdate(detailAccount.getFullName());
        detailVoucher.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));

        LocalDate now = LocalDate.now();
        if ((voucher.getStartDay().isBefore(now) || voucher.getStartDay().isEqual(now)) && (voucher.getEndDate().isEqual(now) || voucher.getEndDate().isAfter(now))) {
            detailVoucher.setVoucherStatus(1);
        } else if (voucher.getStartDay().isAfter(now) && voucher.getEndDate().isAfter(now)) {
            detailVoucher.setVoucherStatus(2);
        }

        if (voucher.getQuantity() == 0) {
            detailVoucher.setStatus(0);
        } else {
            detailVoucher.setStatus(1);
        }
        voucherRepository.save(detailVoucher);
        return "redirect:/mangostore/admin/voucher";
    }

    @Override
    public String deleteVoucher(Long idVoucher) {
        Voucher detailVoucher = voucherRepository.findById(idVoucher).orElse(null);
        if (detailVoucher != null) {
            detailVoucher.setStatus(0);
            voucherRepository.save(detailVoucher);
        }
        return "redirect:/mangostore/admin/voucher";
    }

    @Override
    public boolean restoreVoucherAPI(RestoreVoucherRequest request) {
        Voucher voucher = voucherRepository.findById(request.getIdVoucher()).orElse(null);
        if (null != voucher) {
            voucher.setQuantity(request.getQuantity());
            voucher.setStatus(1);
            voucherRepository.save(voucher);
        }
        return true;
    }

    @Override
    public ResponseEntity<String> checkAddVoucher(VoucherRequest request) {
        Voucher detailVoucherByCode = voucherRepository.getVoucherByCodeVoucher(request.getCodeVoucher());
        if (detailVoucherByCode != null) {
            return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
        } else {
            Voucher detailVoucherByName = voucherRepository.getVoucherByNameVoucher(request.getNameVoucher());
            if (detailVoucherByName != null) {
                return new ResponseEntity<>("2", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("0", HttpStatus.OK);
            }
        }
    }

    @Override
    public ResponseEntity<String> checkUpdateVoucher(VoucherRequest request) {
        Voucher detailVoucher = voucherRepository.findById(Long.parseLong(request.getId())).orElse(null);
        assert detailVoucher != null;
        if (!request.getNameVoucher().equals(detailVoucher.getNameVoucher())) {
            Voucher detailVoucherByName = voucherRepository.getVoucherByNameVoucher(request.getNameVoucher());
            if (detailVoucherByName != null) {
                return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("0", HttpStatus.OK);
    }
}