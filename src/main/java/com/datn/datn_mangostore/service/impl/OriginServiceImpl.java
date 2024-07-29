package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Origin;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.OriginRepository;
import com.datn.datn_mangostore.service.OriginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class OriginServiceImpl implements OriginService {
    private final AccountRepository accountRepository;
    private final OriginRepository originRepository;
    private final Gender gender;

    public OriginServiceImpl(AccountRepository accountRepository,
                             OriginRepository originRepository,
                             Gender gender) {
        this.accountRepository = accountRepository;
        this.originRepository = originRepository;
        this.gender = gender;
    }

    @Override
    public String indexOrigin(Model model,
                              HttpSession session,
                              String keyword) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Origin> itemsOrigin = originRepository.getAllOriginByStatus1();
            if (keyword != null) {
                itemsOrigin = originRepository.searchOrigin(keyword);
                model.addAttribute("keyword", keyword);
            }
            model.addAttribute("listOrigin", itemsOrigin);

            List<Origin> itemsOriginInactive = originRepository.getAllOriginByStatus0();
            model.addAttribute("listOriginInactive", itemsOriginInactive);

            model.addAttribute("addOrigin", new Origin());
            return "admin/origin/IndexOrigin";
        }
    }

    @Override
    public String addOrigin(Origin addOrigin,
                            BindingResult result,
                            HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);

        Origin newOrigin = new Origin();
        newOrigin.setCodeOrigin(gender.generateCode());
        newOrigin.setNameOrigin(addOrigin.getNameOrigin());
        newOrigin.setNameUserCreate(detailAccount.getFullName());
        newOrigin.setNameUserUpdate(detailAccount.getFullName());
        newOrigin.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newOrigin.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newOrigin.setStatus(1);
        originRepository.save(newOrigin);
        return "redirect:/mangostore/admin/origin";
    }

    @Override
    public String detailOrigin(Model model,
                               HttpSession session,
                               Long idOrigin) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Origin> itemsOriginInactive = originRepository.getAllOriginByStatus0();
            model.addAttribute("listOriginInactive", itemsOriginInactive);

            Origin detailOrigin = originRepository.findById(idOrigin).orElse(null);
            model.addAttribute("detailOrigin", detailOrigin);
            return "admin/origin/DetailOrigin";
        }
    }

    @Override
    public String updateOrigin(BindingResult result,
                               HttpSession session,
                               Origin origin) {
        Origin detailOrigin = originRepository.findById(origin.getId()).orElse(null);
        assert detailOrigin != null;
        detailOrigin.setNameOrigin(origin.getNameOrigin());

        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        detailOrigin.setNameUserUpdate(detailAccount.getFullName());
        detailOrigin.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));


        originRepository.save(detailOrigin);
        return "redirect:/mangostore/admin/origin";
    }

    @Override
    public String deleteOrigin(Long idOrigin) {
        Origin detailOrigin = originRepository.findById(idOrigin).orElse(null);
        assert detailOrigin != null;
        detailOrigin.setStatus(0);
        originRepository.save(detailOrigin);
        return "redirect:/mangostore/admin/origin";
    }

    @Override
    public String restoreOrigin(Long idOrigin) {
        Origin detailOrigin = originRepository.findById(idOrigin).orElse(null);
        assert detailOrigin != null;
        detailOrigin.setStatus(1);
        originRepository.save(detailOrigin);
        return "redirect:/mangostore/admin/origin";
    }

    @Override
    public Integer findByOriginCreateExit(String name) {
        Origin origin = originRepository.findByNameExit(name);
        if (origin != null) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public Integer findByOriginUpdateExit(String name, String codeOrigin) {
        Origin originByName = originRepository.findByNameExit(name);
        if (originByName != null) {
            if (originByName.getCodeOrigin().equals(codeOrigin)) {
                return 1;
            } else {
                return 2;
            }
        } else {
            return 3;
        }
    }
}
