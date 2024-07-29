package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Material;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.MaterialRepository;
import com.datn.datn_mangostore.service.MaterialService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MaterialServiceImpl implements MaterialService {
    private final AccountRepository accountRepository;
    private final MaterialRepository materialRepository;
    private final Gender gender;

    public MaterialServiceImpl(AccountRepository accountRepository,
                               MaterialRepository materialRepository,
                               Gender gender) {
        this.accountRepository = accountRepository;
        this.materialRepository = materialRepository;
        this.gender = gender;
    }

    @Override
    public String indexMaterial(Model model,
                                HttpSession session,
                                String keyword) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Material> itemsMaterial = materialRepository.getAllMaterialByStatus1();
            if (keyword != null) {
                itemsMaterial = materialRepository.searchMaterial(keyword);
                model.addAttribute("keyword", keyword);
            }
            model.addAttribute("listMaterial", itemsMaterial);

            List<Material> itemsMaterialInactive = materialRepository.getAllMaterialByStatus0();
            model.addAttribute("listMaterialInactive", itemsMaterialInactive);

            model.addAttribute("addMaterial", new Material());
            return "admin/material/IndexMaterial";
        }
    }

    @Override
    public String addMaterial(Material addMaterial,
                              BindingResult result,
                              HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);

        Material newMaterial = new Material();
        newMaterial.setCodeMaterial(gender.generateCode());
        newMaterial.setNameMaterial(addMaterial.getNameMaterial());
        newMaterial.setNameUserCreate(detailAccount.getFullName());
        newMaterial.setNameUserUpdate(detailAccount.getFullName());
        newMaterial.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newMaterial.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newMaterial.setStatus(1);
        materialRepository.save(newMaterial);
        return "redirect:/mangostore/admin/material";
    }

    @Override
    public String detailMaterial(Model model,
                                 HttpSession session,
                                 Long idMaterial) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Material> itemsMaterialInactive = materialRepository.getAllMaterialByStatus0();
            model.addAttribute("listMaterialInactive", itemsMaterialInactive);

            Material detailMaterial = materialRepository.findById(idMaterial).orElse(null);
            model.addAttribute("detailMaterial", detailMaterial);
            return "admin/material/DetailMaterial";
        }
    }

    @Override
    public String updateMaterial(BindingResult result,
                                 HttpSession session,
                                 Material material) {
        Material detailMaterial = materialRepository.findById(material.getId()).orElse(null);
        assert detailMaterial != null;
        detailMaterial.setNameMaterial(material.getNameMaterial());

        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        detailMaterial.setNameUserUpdate(detailAccount.getFullName());
        detailMaterial.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        materialRepository.save(detailMaterial);
        return "redirect:/mangostore/admin/material";
    }

    @Override
    public String deleteMaterial(Long idMaterial) {
        Material detailMaterial = materialRepository.findById(idMaterial).orElse(null);
        assert detailMaterial != null;
        detailMaterial.setStatus(0);
        materialRepository.save(detailMaterial);
        return "redirect:/mangostore/admin/material";
    }

    @Override
    public String restoreMaterial(Long idMaterial) {
        Material detailMaterial = materialRepository.findById(idMaterial).orElse(null);
        assert detailMaterial != null;
        detailMaterial.setStatus(1);
        materialRepository.save(detailMaterial);
        return "redirect:/mangostore/admin/material";
    }

    @Override
    public Integer findByMaterialCreateExit(String name) {
        Material material = materialRepository.findByNameExit(name);
        if (material != null) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public Integer findByMaterialUpdateExit(String name, String codeMaterial) {
        Material materialByName = materialRepository.findByNameExit(name);
        if (materialByName != null) {
            if (materialByName.getCodeMaterial().equals(codeMaterial)) {
                return 1;
            } else {
                return 2;
            }
        } else {
            return 3;
        }
    }
}
