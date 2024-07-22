package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.AddressClient;
import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.AddressClientRepository;
import com.datn.datn_mangostore.repository.RoleRepository;
import com.datn.datn_mangostore.service.AddressClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AddressClientServiceImpl implements AddressClientService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final AddressClientRepository addressClientRepository;
    private final Gender gender;

    public AddressClientServiceImpl(AccountRepository accountRepository,
                                    RoleRepository roleRepository,
                                    AddressClientRepository addressClientRepository,
                                    Gender gender) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.addressClientRepository = addressClientRepository;
        this.gender = gender;
    }

    @Override
    public String indexAddressClient(Model model, HttpSession session, String keyword) {
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

                List<AddressClient> itemsAddressClient = addressClientRepository.getAllAddressClientByStatus1();
                if (keyword != null) {
                    itemsAddressClient = addressClientRepository.searchNameClient(keyword);
                    model.addAttribute("keyword", keyword);
                }
                model.addAttribute("listAddressClient", itemsAddressClient);

                List<AddressClient> itemsAddressClientInactive = addressClientRepository.getAllAddressClientByStatus0();
                model.addAttribute("listAddressClientInactive", itemsAddressClientInactive);

                model.addAttribute("addAddressClient", new AddressClient());
                return "admin/addressClient/IndexAddressClient";
            }
        }
    }

    @Override
    public String addAddressClient(AddressClient addAddressClient, BindingResult result, HttpSession session) {
        AddressClient newAddressClient = new AddressClient();
        newAddressClient.setCodeAddress(gender.generateCode());
        newAddressClient.setNameClient(addAddressClient.getNameClient());
        newAddressClient.setPhoneNumber(addAddressClient.getPhoneNumber());
        newAddressClient.setSpecificAddress(addAddressClient.getSpecificAddress());
        newAddressClient.setCommune(addAddressClient.getCommune());
        newAddressClient.setDistrict(addAddressClient.getDistrict());
        newAddressClient.setProvince(addAddressClient.getProvince());

//        String email = (String) session.getAttribute("loginEmail");
//        Account detailAccount = accountRepository.detailAccountByEmail(email);
//        newAddressClient.setSaveByAccount(detailAccount.getId());
        newAddressClient.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newAddressClient.setStatus(1);
        addressClientRepository.save(newAddressClient);
        return "redirect:/mangostore/admin/address-client";
    }

    @Override
    public String editAddressClient(Long idAddressClient, Model model, HttpSession session) {
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

                List<AddressClient> itemsAddressClientInactive = addressClientRepository.getAllAddressClientByStatus0();
                model.addAttribute("listAddressClientInactive", itemsAddressClientInactive);

                AddressClient detailAddressClient = addressClientRepository.findById(idAddressClient).orElse(null);
                model.addAttribute("editAddressClient", detailAddressClient);
                return "admin/addressClient/DetailAddressClient";
            }
        }
    }

    @Override
    public String updateAddressClient(AddressClient editAddressClient, BindingResult result) {
        AddressClient detailAddressClient = addressClientRepository.findById(editAddressClient.getId()).orElse(null);

        detailAddressClient.setNameClient(editAddressClient.getNameClient());
        detailAddressClient.setPhoneNumber(editAddressClient.getPhoneNumber());
        detailAddressClient.setSpecificAddress(editAddressClient.getSpecificAddress());
        if (editAddressClient.getCommune().equals("") || editAddressClient.getDistrict().equals("") || editAddressClient.getProvince().equals("")) {
            detailAddressClient.setCommune(detailAddressClient.getCommune());
            detailAddressClient.setDistrict(detailAddressClient.getDistrict());
            detailAddressClient.setProvince(detailAddressClient.getProvince());
        } else {
            detailAddressClient.setCommune(editAddressClient.getCommune());
            detailAddressClient.setDistrict(editAddressClient.getDistrict());
            detailAddressClient.setProvince(editAddressClient.getProvince());
        }
        detailAddressClient.setStatus(editAddressClient.getStatus());

        addressClientRepository.save(detailAddressClient);
        return "redirect:/mangostore/admin/address-client";
    }

    @Override
    public String deleteAddressClient(Long idAddressClient) {
        AddressClient detailAddressClient = addressClientRepository.findById(idAddressClient).orElse(null);
        assert detailAddressClient != null;
        detailAddressClient.setStatus(0);
        addressClientRepository.save(detailAddressClient);
        return "redirect:/mangostore/admin/address-client";
    }

    @Override
    public String restoreAddressClient(Long idAddressClient) {
        AddressClient detailAddressClient = addressClientRepository.findById(idAddressClient).orElse(null);
        assert detailAddressClient != null;
        detailAddressClient.setStatus(1);
        addressClientRepository.save(detailAddressClient);
        return "redirect:/mangostore/admin/address-client";
    }
}
