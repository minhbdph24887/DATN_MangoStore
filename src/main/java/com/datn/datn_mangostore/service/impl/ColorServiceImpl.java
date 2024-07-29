package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Color;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.ColorRepository;
import com.datn.datn_mangostore.service.ColorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ColorServiceImpl implements ColorService {
    private final AccountRepository accountRepository;
    private final ColorRepository colorRepository;
    private final Gender gender;

    public ColorServiceImpl(AccountRepository accountRepository,
                            ColorRepository colorRepository,
                            Gender gender) {
        this.accountRepository = accountRepository;
        this.colorRepository = colorRepository;
        this.gender = gender;
    }

    @Override
    public String indexColor(Model model,
                             HttpSession session,
                             String keyword) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Color> itemsColor = colorRepository.getAllColorByStatus1();
            if (keyword != null) {
                itemsColor = colorRepository.searchColor(keyword);
                model.addAttribute("keyword", keyword);
            }
            model.addAttribute("listColor", itemsColor);

            List<Color> itemsColorInactive = colorRepository.getAllColorByStatus0();
            model.addAttribute("listColorInactive", itemsColorInactive);

            model.addAttribute("addColor", new Color());
            return "admin/color/IndexColor";
        }
    }

    @Override
    public String addColor(Color addColor,
                           BindingResult result,
                           HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        Color newColor = new Color();
        newColor.setCodeColor(gender.generateCode());
        newColor.setNameColor(addColor.getNameColor());
        newColor.setNameUserCreate(detailAccount.getFullName());
        newColor.setNameUserUpdate(detailAccount.getFullName());
        newColor.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newColor.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newColor.setStatus(1);
        colorRepository.save(newColor);
        return "redirect:/mangostore/admin/color";
    }

    @Override
    public String detailColor(Model model,
                              HttpSession session,
                              Long idColor) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Color> itemsColorInactive = colorRepository.getAllColorByStatus0();
            model.addAttribute("listColorInactive", itemsColorInactive);

            Color detailColor = colorRepository.findById(idColor).orElse(null);
            model.addAttribute("detailColor", detailColor);
            return "admin/color/DetailColor";
        }
    }

    @Override
    public String updateColor(BindingResult result,
                              HttpSession session,
                              Color color) {
        Color detailColor = colorRepository.findById(color.getId()).orElse(null);
        assert detailColor != null;
        detailColor.setNameColor(color.getNameColor());

        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        detailColor.setNameUserUpdate(detailAccount.getFullName());
        detailColor.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));

        colorRepository.save(detailColor);
        return "redirect:/mangostore/admin/color";
    }

    @Override
    public String deleteColor(Long idColor) {
        Color detailColor = colorRepository.findById(idColor).orElse(null);
        assert detailColor != null;
        detailColor.setStatus(0);
        colorRepository.save(detailColor);
        return "redirect:/mangostore/admin/color";
    }

    @Override
    public String restoreColor(Long idColor) {
        Color detailColor = colorRepository.findById(idColor).orElse(null);
        assert detailColor != null;
        detailColor.setStatus(1);
        colorRepository.save(detailColor);
        return "redirect:/mangostore/admin/color";
    }

    @Override
    public Integer findByColorCreateExit(String name) {
        Color color = colorRepository.findByNameExit(name);
        if (color != null) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public Integer findByColorUpdateExit(String name, String codeColor) {
        Color colorByName = colorRepository.findByNameExit(name);
        if (colorByName != null) {
            if (colorByName.getCodeColor().equals(codeColor)) {
                return 1;
            } else {
                return 2;
            }
        } else {
            return 3;
        }
    }
}
