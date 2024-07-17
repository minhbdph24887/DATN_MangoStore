package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.bean.Rank;
import com.datn.datn_mangostore.request.NameRankRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface RankService {
    String indexRank(Model model,
                     HttpSession session,
                     String keyword);

    String addRank(Rank addRank,
                   BindingResult result,
                   HttpSession session);

    String detailRank(Model model,
                      HttpSession session,
                      Long idRank);

    String updateRank(HttpSession session,
                      Rank rank);

    String deleteRank(Long idRank);

    String restoreRank(Long idRank);

    ResponseEntity<String> checkAddRank(NameRankRequest request);

    ResponseEntity<String> checkUpdateRank(NameRankRequest request);
}
