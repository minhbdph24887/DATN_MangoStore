package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Category;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.CategoryRepository;
import com.datn.datn_mangostore.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final Gender gender;

    public CategoryServiceImpl(AccountRepository accountRepository,
                               CategoryRepository categoryRepository,
                               Gender gender) {
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.gender = gender;
    }

    @Override
    public String indexCategory(Model model,
                                HttpSession session,
                                String keyword) {
        Account detailAccount = gender.checkMenuAdmin(model, session, "Admin | Trang chủ danh mục");
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Category> itemsCategory = categoryRepository.getAllCategoryByStatus1();
            if (keyword != null) {
                itemsCategory = categoryRepository.searchCategory(keyword);
                model.addAttribute("keyword", keyword);
            }
            model.addAttribute("listCategory", itemsCategory);

            List<Category> itemsCategoryInactive = categoryRepository.getAllCategoryByStatus0();
            model.addAttribute("listCategoryInactive", itemsCategoryInactive);

            model.addAttribute("addCategory", new Category());
            return "admin/category/IndexCategory";
        }
    }

    @Override
    public String addCategory(Category addCategory,
                              BindingResult result,
                              HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        Category newCategory = new Category();
        newCategory.setCodeCategory(gender.generateCode());
        newCategory.setNameCategory(addCategory.getNameCategory());
        newCategory.setNameUserCreate(detailAccount.getFullName());
        newCategory.setNameUserUpdate(detailAccount.getFullName());
        newCategory.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newCategory.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newCategory.setStatus(1);
        categoryRepository.save(newCategory);
        return "redirect:/mangostore/admin/category";
    }

    @Override
    public String detailCategory(Model model,
                                 HttpSession session,
                                 Long idCategory) {
        Account detailAccount = gender.checkMenuAdmin(model, session, "Admin | Cập nhật danh mục");
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Category> itemsCategoryInactive = categoryRepository.getAllCategoryByStatus0();
            model.addAttribute("listCategoryInactive", itemsCategoryInactive);

            Category detailCategory = categoryRepository.findById(idCategory).orElse(null);
            model.addAttribute("detailCategory", detailCategory);
            return "admin/category/DetailCategory";
        }
    }

    @Override
    public String updateCategory(BindingResult result,
                                 HttpSession session,
                                 Category category) {
        Category detailCategory = categoryRepository.findById(category.getId()).orElse(null);
        assert detailCategory != null;
        detailCategory.setNameCategory(category.getNameCategory());

        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        detailCategory.setNameUserUpdate(detailAccount.getFullName());
        detailCategory.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));

        categoryRepository.save(detailCategory);
        return "redirect:/mangostore/admin/category";
    }

    @Override
    public String deleteCategory(Long idCategory) {
        Category detailCategory = categoryRepository.findById(idCategory).orElse(null);
        assert detailCategory != null;
        detailCategory.setStatus(0);
        categoryRepository.save(detailCategory);
        return "redirect:/mangostore/admin/category";
    }

    @Override
    public String restoreCategory(Long idCategory) {
        Category detailCategory = categoryRepository.findById(idCategory).orElse(null);
        assert detailCategory != null;
        detailCategory.setStatus(1);
        categoryRepository.save(detailCategory);
        return "redirect:/mangostore/admin/category";
    }

    @Override
    public Integer findByCategoryCreateExit(String name) {
        Category category = categoryRepository.findByNameExit(name);
        if (category != null) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public Integer findByCategoryUpdateExit(String name, String codeCategory) {
        Category categoryByName = categoryRepository.findByNameExit(name);
        if (categoryByName != null) {
            if (categoryByName.getCodeCategory().equals(codeCategory)) {
                return 1;
            } else {
                return 2;
            }
        } else {
            return 3;
        }
    }
}
