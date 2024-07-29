package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.bean.Category;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface CategoryService {
    String indexCategory(Model model,
                         HttpSession session,
                         String keyword);

    String addCategory(Category addCategory,
                       BindingResult result,
                       HttpSession session);

    String detailCategory(Model model,
                          HttpSession session,
                          Long idCategory);

    String updateCategory(BindingResult result,
                          HttpSession session,
                          Category category);

    String deleteCategory(Long idCategory);

    String restoreCategory(Long idCategory);

    Integer findByCategoryCreateExit(String name);

    Integer findByCategoryUpdateExit(String codeCategory,
                                     String name);
}
