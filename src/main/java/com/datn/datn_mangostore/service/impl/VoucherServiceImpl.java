package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Rank;
import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.bean.Voucher;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.RankRepository;
import com.datn.datn_mangostore.repository.RoleRepository;
import com.datn.datn_mangostore.repository.VoucherRepository;
import com.datn.datn_mangostore.request.RestoreVoucherRequest;
import com.datn.datn_mangostore.service.VoucherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class VoucherServiceImpl implements VoucherService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final VoucherRepository voucherRepository;
    private final RankRepository rankRepository;
    private final Gender gender;
    public VoucherServiceImpl(AccountRepository accountRepository,
                              RoleRepository roleRepository,
                              VoucherRepository voucherRepository,
                              RankRepository rankRepository,
                              Gender gender) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.voucherRepository = voucherRepository;
        this.rankRepository = rankRepository;
        this.gender = gender;
    }

    @Override
    public String indexVoucher(Model model,
                               HttpSession session,
                               String keyword) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            if (detailAccount.getStatus() == 0) {
                session.invalidate();
                return "redirect:/mangostore/home";
            } else {
                return setDataIndex(detailAccount, model, "", email, null, "HOME");
            }
        }
    }

    @Override
    public String addVoucher(Voucher addVoucher,
                             BindingResult result, RedirectAttributes redirectAttributes,
                             HttpSession session, Model model) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);

        createOrUpdateVoucher(addVoucher, detailAccount, "ADD");
        model.addAttribute("successMessage", true);
        return "redirect:/mangostore/admin/voucher";
    }


    @Override
    public String detailVoucher(Model model,
                                HttpSession session,
                                Long idVoucher) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null || idVoucher == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            return setDataIndex(detailAccount, model, "", email, idVoucher, "DETAIL");
        }
    }

    @Override
    public String updateVoucher(RedirectAttributes redirectAttributes,
                                HttpSession session,
                                Voucher voucher, Model model,String reducedValueValidate, String minimumPriceValidate) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        voucher.setStatus(1);
        createOrUpdateVoucher(voucher, detailAccount, "UPDATE");
        redirectAttributes.addFlashAttribute("successMessage", "Update data success");
        return "redirect:/mangostore/admin/voucher";
    }

    @Override
    public String deleteVoucher(RedirectAttributes redirectAttributes, Long idVoucher) {
        Voucher detailVoucher = voucherRepository.findById(idVoucher).orElse(null);
        if (detailVoucher != null) {
            detailVoucher.setStatus(0);
            voucherRepository.save(detailVoucher);
        }
        redirectAttributes.addFlashAttribute("successMessage", "Update successfully !!");
        return "redirect:/mangostore/admin/voucher";
    }

    @Override
    public String searchVoucher(Model model, HttpSession session, String keyword) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        return setDataIndex(detailAccount, model, keyword, email, null, "SEARCH");

    }

    @Override
    public String searchVoucherInactive(Model model, HttpSession session, String keyword) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        return setDataIndex(detailAccount, model, keyword, email, null, "SEARCH-INACTIVE");

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

    private void createOrUpdateVoucher(Voucher addVoucher, Account detailAccount, String function) {
        if ("ADD".equals(function)) {
            Voucher newVoucher = new Voucher();
            if (addVoucher.getCodeVoucher().isEmpty()) {
                newVoucher.setCodeVoucher(gender.generateCode());
            } else {
                newVoucher.setCodeVoucher(addVoucher.getCodeVoucher());
            }
            newVoucher.setNameVoucher(addVoucher.getNameVoucher());
            newVoucher.setQuantity(addVoucher.getQuantity());
            newVoucher.setReducedValue(addVoucher.getReducedValue());
            newVoucher.setVoucherFrom(addVoucher.getVoucherFrom());
            newVoucher.setMinimumPrice(addVoucher.getMinimumPrice());
            newVoucher.setRank(addVoucher.getRank());
            newVoucher.setStartDay(addVoucher.getStartDay());
            newVoucher.setEndDate(addVoucher.getEndDate());
            duplicateCode(addVoucher, detailAccount, newVoucher);
            newVoucher.setStatus(1);
            voucherRepository.save(newVoucher);

        } else if ("UPDATE".equals(function)) {
            Voucher detailVoucher = voucherRepository.findById(addVoucher.getId()).orElse(null);
            if (detailAccount != null && detailVoucher != null) {
                detailVoucher.setCodeVoucher(addVoucher.getCodeVoucher());
                detailVoucher.setNameVoucher(addVoucher.getNameVoucher());
                detailVoucher.setQuantity(addVoucher.getQuantity());
                detailVoucher.setReducedValue(addVoucher.getReducedValue());
                detailVoucher.setVoucherFrom(addVoucher.getVoucherFrom());
                detailVoucher.setRank(addVoucher.getRank());
                detailVoucher.setStartDay(addVoucher.getStartDay());
                detailVoucher.setEndDate(addVoucher.getEndDate());
                detailVoucher.setMinimumPrice(addVoucher.getMinimumPrice());
                detailVoucher.setUserCreate(detailAccount.getFullName());
                duplicateCode(detailVoucher, detailAccount, detailVoucher);
                detailVoucher.setStatus(addVoucher.getStatus());
                if (addVoucher.getQuantity() == 0){
                    detailVoucher.setStatus(0);
                }
                voucherRepository.save(detailVoucher);
            }

        }
    }

    private void duplicateCode(Voucher addVoucher, Account detailAccount, Voucher newVoucher) {
        newVoucher.setUserCreate(detailAccount.getFullName());
        newVoucher.setUserUpdate(detailAccount.getFullName());
        newVoucher.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime()
                , DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newVoucher.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime()
                , DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        int checkStatus = 0;
        if (addVoucher.getStartDay().equals(LocalDate.now())) {
            checkStatus = 1;
        } else if (addVoucher.getStartDay().isAfter(LocalDate.now())) {
            checkStatus = 2;
        }
        newVoucher.setVoucherStatus(checkStatus);
    }

    private String checkDataRequest(Voucher voucher, Model model, Account detailAccount, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        model.addAttribute("isFunctionVisible", true);
        if (!"".equals(voucher.getCodeVoucher())) {
            String regexCodeVoucher = "^(?=.*[A-Z])(?=.*\\d).{10}$";
            if (!voucher.getCodeVoucher().matches(regexCodeVoucher)){
                model.addAttribute("errorMessage", "Voucher Code must not be written in lowercase" +
                        " letters and must have 10 characters.");
            }
            var voucherByCode = voucherRepository.findByCodeVoucher(voucher.getCodeVoucher());
            if (!voucherByCode.isEmpty()) {
                model.addAttribute("errorMessage", "Voucher Code already exists");
                return setDataIndex(detailAccount, model, "", email, null, "HOME");
            }
            model.addAttribute("errorMessage", "Voucher Code must not be written in lowercase" +
                    " letters and must have 10 characters.");
            return setDataIndex(detailAccount, model, "", email, null, "HOME");
        }

        var voucherByCode = voucherRepository.findByCodeVoucher(voucher.getCodeVoucher());
        if (!voucherByCode.isEmpty()) {
            model.addAttribute("errorMessage", "Voucher Code already exists");
            return setDataIndex(detailAccount, model, "", email, null, "HOME");
        }
        if (StringUtils.hasLength(voucher.getNameVoucher())) {
            var voucherByName = voucherRepository.findByNameVoucher(voucher.getNameVoucher());
            if (!voucherByName.isEmpty()) {
                model.addAttribute("errorMessage", "Voucher Name already exists");
                return setDataIndex(detailAccount, model, "", email, null, "HOME");
            }
        }
        if (!StringUtils.hasLength(voucher.getNameVoucher())) {
            model.addAttribute("errorMessage", "Invalid name voucher");
            return setDataIndex(detailAccount, model, "", email, null, "HOME");
        }
        if (voucher.getQuantity() <= 0 || voucher.getReducedValue() <= 0 || voucher.getMinimumPrice() <= 0) {
            model.addAttribute("errorMessage", "Number greater than 0");
            return setDataIndex(detailAccount, model, "", email, null, "HOME");
        }
        if (null == voucher.getStartDay()) {
            model.addAttribute("errorMessage", "Invalid start day");
            return setDataIndex(detailAccount, model, "", email, null, "HOME");
        }
        if (null == voucher.getEndDate()) {
            model.addAttribute("errorMessage", "Invalid end date");
            return setDataIndex(detailAccount, model, "", email, null, "HOME");
        }
        if (voucher.getStartDay().isAfter(voucher.getEndDate())) {
            model.addAttribute("errorMessage", "End date must be after start date");
            return setDataIndex(detailAccount, model, "", email, null, "HOME");
        }
        return null;
    }

    public String setDataIndex(Account detailAccount, Model model, String keyword,
                               String email, Long idVoucher, String function) {



        LocalDateTime checkDate = LocalDateTime.now();
        int hour = checkDate.getHour();
        if (hour >= 5 && hour < 10) {
            model.addAttribute("dates", "Morning");
        } else if (hour >= 10 && hour < 13) {
            model.addAttribute("dates", "Noon");
        } else if (hour >= 13 && hour < 18) {
            model.addAttribute("dates", "Afternoon");
        } else {
            model.addAttribute("dates", "Evening");
        }
        Role detailRole = roleRepository.getRoleByEmail(email);
        if ("ADMIN".equals(detailRole.getName())) {
            model.addAttribute("checkMenuAdmin", true);
        } else {
            model.addAttribute("checkMenuAdmin", false);
        }

        List<Rank> itemsRank = rankRepository.getAllRankByStatus1();
        model.addAttribute("listRank", itemsRank);
        model.addAttribute("profile", detailAccount);
        String regex = "^(?=.*[^a-z]).{10}$";
        switch (function) {
            case "DETAIL" -> {
                Voucher detailVoucher = voucherRepository.findById(idVoucher).orElse(null);
                model.addAttribute("detailVoucher", detailVoucher);
                return "admin/voucher/DetailVoucher";
            }
            case "SEARCH" -> {
                List<Voucher> voucherList;
                if (keyword != null && keyword.matches(regex)) {
                    voucherList = voucherRepository.findByCodeVoucher(keyword);
                } else {
                    voucherList = voucherRepository.findByNameVoucher(keyword);
                }
                model.addAttribute("listVoucherAll", voucherList);
                model.addAttribute("activeVoucherLst", voucherList);
                return "admin/voucher/IndexVoucher";
            }
            case "SEARCH-INACTIVE" -> {
                List<Voucher> voucherList;
                if (keyword != null && keyword.matches(regex)) {
                    voucherList = voucherRepository.findByCodeVoucherAndStatus(keyword, 0);
                } else {
                    voucherList = voucherRepository.findByNameVoucherAndStatus(keyword, 0);
                }
                model.addAttribute("listVoucherAll", voucherList);
                model.addAttribute("activeVoucherLst", voucherList);
                return "admin/voucher/IndexVoucher";
            }
            case "HOME" -> {
                List<Voucher> listVoucherInactive = voucherRepository.findAllByStatusOrderByDateCreateDesc(0);
                List<Voucher> listVoucherActive = voucherRepository.findAllByStatusOrderByDateCreateDesc(1);

                model.addAttribute("listVoucherAll", listVoucherActive);
                model.addAttribute("listVoucherActive", listVoucherActive);
                model.addAttribute("listVoucherInactive", listVoucherInactive);

                model.addAttribute("listRank", itemsRank);
                return "admin/voucher/IndexVoucher";
            }
            default -> {
                List<Voucher> listVoucherInactive = voucherRepository.findAllByStatusOrderByDateCreateDesc(0);
                List<Voucher> listVoucherActive = voucherRepository.findAllByStatusOrderByDateCreateDesc(1);

                model.addAttribute("listVoucherAll", listVoucherActive);
                model.addAttribute("listVoucherActive", listVoucherActive);
                model.addAttribute("listVoucherInactive", listVoucherInactive);

                model.addAttribute("listRank", itemsRank);
                return "admin/voucher/IndexVoucher";
            }
        }
    }
}
