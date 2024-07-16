package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Rank;
import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.RankRepository;
import com.datn.datn_mangostore.repository.RoleRepository;
import com.datn.datn_mangostore.service.RankService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class RankServiceImpl implements RankService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final RankRepository rankRepository;
    private final Gender gender;
    private final String regex = "^(?=.*[^a-z]).{10}$";


    public RankServiceImpl(AccountRepository accountRepository, RoleRepository roleRepository
            , RankRepository rankRepository, Gender gender) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.rankRepository = rankRepository;
        this.gender = gender;
    }

    @Override
    public String indexRank(Model model,
                            HttpSession session,
                            String keyword) {
        return setDataIndex( model, "HOME", null,session ,"");
    }
    @Override
    public String addRank(Rank addRank, BindingResult result
            , HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("showNotification", true);
        if (!StringUtils.hasLength(addRank.getNameRank())
                || addRank.getMinimumScore() == null
                || addRank.getMaximumScore() == null
                || addRank.getMaximumScore() < addRank.getMinimumScore()){
            model.addAttribute("errorMessage", "Invalid Format");
            return setDataIndex(model, "HOME", null, session , "home");
        }
        var rankLst = rankRepository.findByNameRank(addRank.getNameRank());
        if (!rankLst.isEmpty()){
            model.addAttribute("errorMessage", "Rank Name already exists");
            return setDataIndex(model, "HOME", null, session , "home");
        }
        createOrUpdateRank(addRank, session, "ADD");
        redirectAttributes.addFlashAttribute("successMessage", "Insert Successful");
        return "redirect:/mangostore/admin/rank";
    }


    @Override
    public String detailRank(Model model, RedirectAttributes redirectAttributes,
                             HttpSession session,
                             Long idRank) {
        return setDataIndex(model,"DETAIL", idRank, session , "");
    }

    @Override
    public String updateRank(HttpSession session, Rank rank,
                             Model model, RedirectAttributes redirectAttributes) {
        if (!StringUtils.hasLength(rank.getNameRank())){
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid name rank");
            return setDataIndex(model, "HOME", null, session ,"home");
        }
        if (rank.getMinimumScore() == null){
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid minimum score");
            return setDataIndex(model, "HOME", null, session ,"home");
        }
        if (rank.getMaximumScore() == null){
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid maximum score");
            return setDataIndex(model, "HOME", null, session ,"home");
        }
        if (rank.getMaximumScore() < rank.getMinimumScore()){
            redirectAttributes.addFlashAttribute("errorMessage", "Minimum score must be greater than maximum score");
            return setDataIndex(model, "HOME", null, session ,"home");
        }
        createOrUpdateRank(rank, session, "UPDATE");
        redirectAttributes.addFlashAttribute("successMessage", "Update Successful");
        return "redirect:/mangostore/admin/rank";
    }

    @Override
    public String searchRank(Model model, HttpSession session, String keyword) {
        return setDataIndex(model,"SEARCH", null, session , keyword);
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

    private String setDataIndex(Model model, String function, Long idRank,
                                HttpSession session,String keyword ){

        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
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
                List<Rank> rankLst = rankRepository.getAllRankByStatus1();
                model.addAttribute("listRank", rankLst);

                List<Rank> itemsRankInactive = rankRepository.getAllRankByStatus0();
                model.addAttribute("listRankInactive", itemsRankInactive);
                model.addAttribute("addRank", new Rank());
                if ("SEARCH".equals(function)) {
                    List<Rank> rankList;
                    if (keyword != null && keyword.matches(regex)) {
                        rankList = rankRepository.findByCodeRank(keyword);
                    }else{
                        rankList = rankRepository.findByNameRank(keyword);
                    }
                    model.addAttribute("listRank", rankList);
                    model.addAttribute("listRankInactive", rankList);
                    return "admin/rank/IndexRank";
                }else if("DETAIL".equals(function)){
                    Rank detailRank = rankRepository.findById(idRank).orElse(null);
                    model.addAttribute("detailRank", detailRank);
                    return "admin/rank/DetailRank";
                }

                return "admin/rank/IndexRank";
            }
        }
    }

    private void createOrUpdateRank(Rank addRank, HttpSession session, String function){
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        if ("ADD".equals(function)){
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
        }else if("UPDATE".equals(function)){
            Rank detailRank = rankRepository.findById(addRank.getId()).orElse(null);
            if (detailRank != null){
                detailRank.setNameRank(addRank.getNameRank());
                detailRank.setMinimumScore(addRank.getMinimumScore());
                detailRank.setMaximumScore(addRank.getMaximumScore());
                detailRank.setUserUpdate(detailAccount.getFullName());
                detailRank.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
//                detailRank.setStatus(addRank.getStatus());
                rankRepository.save(detailRank);
            }
        }

    }
}
