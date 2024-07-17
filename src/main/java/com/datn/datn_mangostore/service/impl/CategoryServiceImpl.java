package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Category;
import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.CategoryRepository;
import com.datn.datn_mangostore.repository.RoleRepository;
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
    private final RoleRepository roleRepository;
    private CategoryRepository categoryRepository;
    private final Gender gender;

    public CategoryServiceImpl(AccountRepository accountRepository,
                               RoleRepository roleRepository,
                               CategoryRepository categoryRepository,
                               Gender gender) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.gender = gender;
    }

    @Override
    public String indexCategory(Model model,
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
                model.addAttribute("profile", detailAccount);

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
                if (detailRole.getName().equals("ADMIN")) {
                    model.addAttribute("checkMenuAdmin", true);
                } else {
                    model.addAttribute("checkMenuAdmin", false);
                }

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
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            if (detailAccount.getStatus() == 0) {
                session.invalidate();
                return "redirect:/mangostore/home";
            } else {
                model.addAttribute("profile", detailAccount);

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
                if (detailRole.getName().equals("ADMIN")) {
                    model.addAttribute("checkMenuAdmin", true);
                } else {
                    model.addAttribute("checkMenuAdmin", false);
                }

                List<Category> itemsCategoryInactive = categoryRepository.getAllCategoryByStatus0();
                model.addAttribute("listCategoryInactive", itemsCategoryInactive);

                Category detailCategory = categoryRepository.findById(idCategory).orElse(null);
                model.addAttribute("detailCategory", detailCategory);
                return "admin/category/DetailCategory";
            }
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
            return 1; // Tồn tại
        } else {
            return 2; // Chưa có
        }
    }

    @Override
    public Integer findByCategoryUpdateExit(String name, String codeCategory) {
        Category categoryByName = categoryRepository.findByNameExit(name);
        if (categoryByName != null) {
            // Nếu tìm thấy danh mục có cùng name
            if (categoryByName.getCodeCategory().equals(codeCategory)) {
                return 1; // Danh mục tồn tại và codeCategory trùng khớp (Update)
            } else {
                return 2; // Chỉ có name tồn tại, nhưng codeCategory khác (Name đã tồn tại)
            }
        } else {
            return 3; // Name chưa tồn tại (Create mới)
        }
    }


}
