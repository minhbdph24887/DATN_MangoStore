package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Rank;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.RankRepository;
import com.datn.datn_mangostore.request.NameRankRequest;
import com.datn.datn_mangostore.service.RankService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class RankServiceImpl implements RankService {
    private final AccountRepository accountRepository;
    private final RankRepository rankRepository;
    private final Gender gender;
    private static final String regex = "^(?=.*[^a-z]).{10}$";


    public RankServiceImpl(AccountRepository accountRepository,
                           RankRepository rankRepository,
                           Gender gender) {
        this.accountRepository = accountRepository;
        this.rankRepository = rankRepository;
        this.gender = gender;
    }

    @Override
    public String indexRank(Model model,
                            HttpSession session,
                            String keyword) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Rank> itemsRank = rankRepository.getAllRankByStatus1();
            if (keyword != null) {
                if (keyword.matches(regex)) {
                    itemsRank = rankRepository.searchRankByCode(keyword);
                } else {
                    itemsRank = rankRepository.searchRankByName(keyword);
                }
                model.addAttribute("keyword", keyword);
            }
            model.addAttribute("listRank", itemsRank);

            List<Rank> itemsRankInactive = rankRepository.getAllRankByStatus0();
            model.addAttribute("listRankInactive", itemsRankInactive);

            model.addAttribute("addRank", new Rank());
            return "admin/rank/IndexRank";
        }
    }

    @Override
    public String addRank(Rank addRank,
                          BindingResult result,
                          HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        Rank newRank = new Rank();
        newRank.setCodeRank(gender.generateCode());
        newRank.setNameRank(addRank.getNameRank());
        newRank.setMinimumScore(addRank.getMinimumScore());
        newRank.setMaximumScore(addRank.getMaximumScore());
        newRank.setUserCreate(detailAccount.getFullName());
        newRank.setUserUpdate(detailAccount.getFullName());
        newRank.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newRank.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newRank.setStatus(1);
        rankRepository.save(newRank);
        return "redirect:/mangostore/admin/rank";
    }


    @Override
    public String detailRank(Model model,
                             HttpSession session,
                             Long idRank) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Rank> itemsRankInactive = rankRepository.getAllRankByStatus0();
            model.addAttribute("listRankInactive", itemsRankInactive);

            Rank detailRank = rankRepository.findById(idRank).orElse(null);
            model.addAttribute("detailRank", detailRank);
            return "admin/rank/DetailRank";
        }
    }

    @Override
    public String updateRank(HttpSession session, Rank rank) {
        Rank detailRank = rankRepository.findById(rank.getId()).orElse(null);
        assert detailRank != null;
        detailRank.setNameRank(rank.getNameRank());
        detailRank.setMinimumScore(rank.getMinimumScore());
        detailRank.setMaximumScore(rank.getMaximumScore());
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        detailRank.setUserUpdate(detailAccount.getFullName());
        detailRank.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        rankRepository.save(detailRank);
        return "redirect:/mangostore/admin/rank";
    }

    @Override
    public String deleteRank(Long idRank) {
        Rank detailRank = rankRepository.findById(idRank).orElse(null);
        assert detailRank != null;
        detailRank.setStatus(0);
        rankRepository.save(detailRank);
        return "redirect:/mangostore/admin/rank";
    }

    @Override
    public String restoreRank(Long idRank) {
        Rank detailRank = rankRepository.findById(idRank).orElse(null);
        if (detailRank != null) {
            detailRank.setStatus(1);
            rankRepository.save(detailRank);
        }
        return "redirect:/mangostore/admin/rank";
    }

    @Override
    public ResponseEntity<String> checkAddRank(NameRankRequest request) {
        Rank detailRank = rankRepository.detailRankByNameRank(request.getNameRank());
        if (detailRank != null) {
            return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("0", HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<String> checkUpdateRank(NameRankRequest request) {
        Rank detailRank = rankRepository.findById(Long.parseLong(request.getId())).orElse(null);
        assert detailRank != null;
        if (!request.getNameRank().equals(detailRank.getNameRank())) {
            Rank detailRankByName = rankRepository.detailRankByNameRank(request.getNameRank());
            if (detailRankByName != null) {
                return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("0", HttpStatus.OK);
    }
}
