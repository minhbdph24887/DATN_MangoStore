package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.bean.Rank;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface RankService {
    String indexRank(Model model,
                     HttpSession session,
                     String keyword);

    String addRank(Rank addRank,
                   BindingResult result,
                   HttpSession session, Model model, RedirectAttributes redirectAttributes);

    String detailRank(Model model, RedirectAttributes redirectAttributes,
                      HttpSession session,
                      Long idRank);

    String updateRank(HttpSession session, Rank rank,
                      Model model, RedirectAttributes redirectAttributes);

    String searchRank(Model model, HttpSession session,String keyword);

    String deleteRank(Long idRank);

    String restoreRank(Long idRank);
}
