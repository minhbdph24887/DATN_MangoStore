package com.datn.datn_mangostore.service.impl;


import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Size;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.SizeRepository;
import com.datn.datn_mangostore.service.SizeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SizeServiceImpl implements SizeService {
    private final AccountRepository accountRepository;
    private final SizeRepository sizeRepository;
    private final Gender gender;

    public SizeServiceImpl(AccountRepository accountRepository,
                           SizeRepository sizeRepository,
                           Gender gender) {
        this.accountRepository = accountRepository;
        this.sizeRepository = sizeRepository;
        this.gender = gender;
    }

    @Override
    public String indexSize(Model model,
                            HttpSession session,
                            String keyword) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Size> itemsSize = sizeRepository.getAllSizeByStatus1();
            if (keyword != null) {
                itemsSize = sizeRepository.searchSize(keyword);
                model.addAttribute("keyword", keyword);
            }
            model.addAttribute("listSize", itemsSize);

            List<Size> itemsSizeInactive = sizeRepository.getAllSizeByStatus0();
            model.addAttribute("listSizeInactive", itemsSizeInactive);

            model.addAttribute("addSize", new Size());
            return "admin/size/IndexSize";
        }
    }

    @Override
    public String addSize(Size addSize,
                          BindingResult result,
                          HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        Size newSize = new Size();
        newSize.setCodeSize(gender.generateCode());
        newSize.setNameSize(addSize.getNameSize());
        newSize.setNameUserCreate(detailAccount.getFullName());
        newSize.setNameUserUpdate(detailAccount.getFullName());
        newSize.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newSize.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newSize.setStatus(1);
        sizeRepository.save(newSize);
        return "redirect:/mangostore/admin/size";
    }

    @Override
    public String detailSize(Model model,
                             HttpSession session,
                             Long idSize) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Size> itemsSizeInactive = sizeRepository.getAllSizeByStatus0();
            model.addAttribute("listSizeInactive", itemsSizeInactive);

            Size detailSize = sizeRepository.findById(idSize).orElse(null);
            model.addAttribute("detailSize", detailSize);
            return "admin/size/DetailSize";
        }
    }

    @Override
    public String updateSize(BindingResult result,
                             HttpSession session,
                             Size size) {
        Size detailSize = sizeRepository.findById(size.getId()).orElse(null);
        assert detailSize != null;
        detailSize.setNameSize(size.getNameSize());

        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        detailSize.setNameUserUpdate(detailAccount.getFullName());
        detailSize.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));

        sizeRepository.save(detailSize);
        return "redirect:/mangostore/admin/size";
    }

    @Override
    public String deleteSize(Long idSize) {
        Size detailSize = sizeRepository.findById(idSize).orElse(null);
        assert detailSize != null;
        detailSize.setStatus(0);
        sizeRepository.save(detailSize);
        return "redirect:/mangostore/admin/size";
    }

    @Override
    public String restoreSize(Long idSize) {
        Size detailSize = sizeRepository.findById(idSize).orElse(null);
        assert detailSize != null;
        detailSize.setStatus(1);
        sizeRepository.save(detailSize);
        return "redirect:/mangostore/admin/size";
    }

    @Override
    public Integer findBySizeCreateExit(String name) {
        Size size = sizeRepository.findByNameExit(name);
        if (size != null) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public Integer findBySizeUpdateExit(String name, String codeSize) {
        Size sizeByName = sizeRepository.findByNameExit(name);
        if (sizeByName != null) {
            if (sizeByName.getCodeSize().equals(codeSize)) {
                return 1;
            } else {
                return 2;
            }
        } else {
            return 3;
        }
    }
}
