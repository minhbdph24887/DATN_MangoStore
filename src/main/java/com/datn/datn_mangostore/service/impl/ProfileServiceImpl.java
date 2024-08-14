package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Authentication;
import com.datn.datn_mangostore.bean.Rank;
import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.AuthenticationRepository;
import com.datn.datn_mangostore.repository.RankRepository;
import com.datn.datn_mangostore.repository.RoleRepository;
import com.datn.datn_mangostore.request.AccountRequest;
import com.datn.datn_mangostore.request.DataRequest;
import com.datn.datn_mangostore.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final RankRepository rankRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationRepository authenticationRepository;
    private final Gender gender;

    public ProfileServiceImpl(AccountRepository accountRepository,
                              RoleRepository roleRepository,
                              RankRepository rankRepository,
                              PasswordEncoder encoder,
                              AuthenticationRepository authenticationRepository,
                              Gender gender) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.rankRepository = rankRepository;
        this.encoder = encoder;
        this.authenticationRepository = authenticationRepository;
        this.gender = gender;
    }

    @Override
    public String getAllStaffByStatus1(Model model,
                                       HttpSession session) {
        Account detailAccount = gender.checkMenuAdmin(model, session, "Admin | Trang chủ nhân viên");
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Account> itemsManager = accountRepository.getAllAccountByRoleManagerAndStatus1();
            model.addAttribute("listManager", itemsManager);

            List<Account> itemsStaff = accountRepository.getAllAccountByRoleStaffAndStatus1();
            model.addAttribute("listStaff", itemsStaff);

            List<Account> itemsManagerInactive = accountRepository.getAllAccountByRoleManagerAndStatus0();
            model.addAttribute("listManagerInactive", itemsManagerInactive);

            List<Account> itemsStaffInactive = accountRepository.getAllAccountByRoleStaffAndStatus0();
            model.addAttribute("listStaffInactive", itemsStaffInactive);
            return "admin/staff/IndexStaff";
        }
    }

    @Override
    public String restoreStaff(Long idAccount) {
        Account detailAccount = accountRepository.findById(idAccount).orElse(null);
        assert detailAccount != null;
        detailAccount.setStatus(1);
        accountRepository.save(detailAccount);
        return "redirect:/mangostore/admin/list-staff";
    }

    @Override
    public String restoreClient(Long idAccount) {
        Account detailAccount = accountRepository.findById(idAccount).orElse(null);
        assert detailAccount != null;
        detailAccount.setStatus(1);
        accountRepository.save(detailAccount);
        return "redirect:/mangostore/admin/list-client";
    }

    @Override
    public String detailStaff(Model model,
                              HttpSession session,
                              Long idAccount) {
        Account detailAccount = gender.checkMenuAdmin(model, session, "Admin | Cập nhật nhân viên");
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailProfile = accountRepository.findById(idAccount).orElse(null);
            model.addAttribute("detailProfile", detailProfile);

            assert detailProfile != null;
            String accountRole = roleRepository.getRoleByEmail(detailProfile.getEmail()).getName();
            model.addAttribute("accountRole", accountRole);
            return "admin/staff/DetailStaff";
        }
    }

    @Override
    public String detailViewStaff(Model model,
                                  HttpSession session) {
        Account detailAccount = gender.checkMenuAdmin(model, session, "Admin | Nhân viên");
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            model.addAttribute("detailProfile", detailAccount);
            String accountRole = roleRepository.getRoleByEmail(detailAccount.getEmail()).getName();
            model.addAttribute("accountRole", accountRole);
            return "admin/staff/DetailStaff";
        }
    }

    @Override
    public String detailclient(Model model,
                               HttpSession session,
                               Long idAccount) {
        Account detailAccount = gender.checkMenuAdmin(model, session, "Admin | Cập nhật khách hàng");
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailProfile = accountRepository.findById(idAccount).orElse(null);
            model.addAttribute("detailProfile", detailProfile);

            assert detailProfile != null;
            String accountRole = roleRepository.getRoleByEmail(detailProfile.getEmail()).getName();
            model.addAttribute("accountRole", accountRole);
            return "admin/client/DetailClient";
        }
    }

    @Override
    public String updateStaff(BindingResult result,
                              String newPassword,
                              MultipartFile imageFile,
                              Account account,
                              HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.findById(account.getId()).orElse(null);
        assert detailAccount != null;
        detailAccount.setFullName(account.getFullName());
        detailAccount.setNumberPhone(account.getNumberPhone());
        detailAccount.setEmail(account.getEmail());
        detailAccount.setBirthday(account.getBirthday());
        detailAccount.setGender(account.getGender());

        String oldImagePath = accountRepository.findById(account.getId()).get().getImages();
        if (imageFile.isEmpty()) {
            detailAccount.setImages(oldImagePath);
        } else {
            String fileName = imageFile.getOriginalFilename();
            detailAccount.setImages(fileName);
        }

        detailAccount.setEncryptionPassword(newPassword == null ? detailAccount.getEncryptionPassword() : encoder.encode(newPassword));
        detailAccount.setStatus(account.getStatus());
        accountRepository.save(detailAccount);
        Role detailRole = roleRepository.getRoleByEmail(email);
        if (detailRole.getName().equals("ADMIN")) {
            return "redirect:/mangostore/admin/list-staff";
        } else {
            return "redirect:/mangostore/admin/list-staff/detail-view";
        }
    }

    @Override
    public String updateClient(BindingResult result,
                               String newPassword,
                               MultipartFile imageFile,
                               Account account,
                               HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.findById(account.getId()).orElse(null);
        assert detailAccount != null;
        detailAccount.setFullName(account.getFullName());
        detailAccount.setNumberPhone(account.getNumberPhone());
        detailAccount.setEmail(account.getEmail());
        detailAccount.setBirthday(account.getBirthday());
        detailAccount.setGender(account.getGender());

        String oldImagePath = accountRepository.findById(account.getId()).get().getImages();
        if (imageFile.isEmpty()) {
            detailAccount.setImages(oldImagePath);
        } else {
            String fileName = imageFile.getOriginalFilename();
            detailAccount.setImages(fileName);
        }

        detailAccount.setEncryptionPassword(newPassword == null ? detailAccount.getEncryptionPassword() : encoder.encode(newPassword));
        detailAccount.setStatus(account.getStatus());
        accountRepository.save(detailAccount);
        Role detailRole = roleRepository.getRoleByEmail(email);
        if (detailRole.getName().equals("ADMIN")) {
            return "redirect:/mangostore/admin/list-client";
        } else {
            return "redirect:/mangostore/admin/list-client/detail-view";
        }
    }

    @Override
    public String deleteStaff(Long idAccount) {
        Account detailAccount = accountRepository.findById(idAccount).orElse(null);
        assert detailAccount != null;
        detailAccount.setStatus(0);
        accountRepository.save(detailAccount);
        return "redirect:/mangostore/admin/list-staff";
    }

    @Override
    public String deleteClient(Long idAccount) {
        Account detailAccount = accountRepository.findById(idAccount).orElse(null);
        assert detailAccount != null;
        detailAccount.setStatus(0);
        accountRepository.save(detailAccount);
        return "redirect:/mangostore/admin/list-client";
    }

    @Override
    public String addAccountClient(BindingResult result,
                                   Account addClient) {
        Account newAccount = new Account();
        newAccount.setFullName(addClient.getFullName());
        newAccount.setNumberPhone(addClient.getNumberPhone());
        newAccount.setEmail(addClient.getEmail());
        newAccount.setBirthday(addClient.getBirthday());
        newAccount.setGender(addClient.getGender());
        newAccount.setImages(addClient.getImages());
        newAccount.setEncryptionPassword(encoder.encode(addClient.getPassword()));
        newAccount.setAccumulatedPoints(0);
        Rank detailRank = rankRepository.detailRankByAccumulatedPoints(0);
        newAccount.setRank(detailRank);
        newAccount.setStatus(1);
        accountRepository.save(newAccount);

        Role roleUser = roleRepository.getAllRoleByUser();
        Authentication newAuthentication = new Authentication();
        newAuthentication.setAccount(newAccount);
        newAuthentication.setRole(roleUser);
        authenticationRepository.save(newAuthentication);
        return "redirect:/mangostore/admin/list-client";
    }


    @Override
    public String addAccountStaff(BindingResult result,
                                  Account addProfile) {
        Account newAccount = new Account();
        newAccount.setFullName(addProfile.getFullName());
        newAccount.setNumberPhone(addProfile.getNumberPhone());
        newAccount.setEmail(addProfile.getEmail());
        newAccount.setBirthday(addProfile.getBirthday());
        newAccount.setGender(addProfile.getGender());
        newAccount.setImages(addProfile.getImages());
        newAccount.setEncryptionPassword(encoder.encode(addProfile.getPassword()));
        newAccount.setAccumulatedPoints(0);
        Rank detailRank = rankRepository.detailRankByAccumulatedPoints(0);
        newAccount.setRank(detailRank);
        newAccount.setStatus(1);
        accountRepository.save(newAccount);
        Role detailRoleStaff = roleRepository.getAllRoleByStaff();
        Authentication newAuthentication = new Authentication();
        newAuthentication.setAccount(newAccount);
        newAuthentication.setRole(detailRoleStaff);
        authenticationRepository.save(newAuthentication);
        return "redirect:/mangostore/admin/list-staff";
    }

    @Override
    public String getAllAccountByClient(Model model,
                                        HttpSession session) {
        Account detailAccount = gender.checkMenuAdmin(model, session, "Admin | Trang chủ khách hàng");
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Account> itemsAccount = accountRepository.getAllAccountByClient();
            model.addAttribute("listClient", itemsAccount);

            List<Account> itemsAccountInactive = accountRepository.getAllAccountByClient0();
            model.addAttribute("listClientInactive", itemsAccountInactive);
            return "admin/client/IndexClient";
        }
    }

    @Override
    public ResponseEntity<String> checkAddAccount(DataRequest request) {
        Account detailAccountByNumberPhone = accountRepository.findAccountByNumberPhone(request.getNumberPhone());
        if (detailAccountByNumberPhone != null) {
            return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
        } else {
            Account detailAccountByEmail = accountRepository.detailAccountByEmail(request.getEmail());
            if (detailAccountByEmail != null) {
                return new ResponseEntity<>("2", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("0", HttpStatus.OK);
            }
        }
    }

    @Override
    public ResponseEntity<String> checkUpdateAccount(DataRequest request) {
        Account currentAccount = accountRepository.findById(Long.parseLong(request.getIdUpdate())).orElse(null);
        Integer countedAccountByRoleAdmin = authenticationRepository.countAccountByRoleAdmin();
        boolean isCurrentAdmin = authenticationRepository.isAdminAccount(Long.parseLong(request.getIdUpdate()));
        assert currentAccount != null;

        if (!Objects.equals(currentAccount.getNumberPhone(), request.getNumberPhone())) {
            Account detailAccountByNumberPhone = accountRepository.findAccountByNumberPhone(request.getNumberPhone());
            if (detailAccountByNumberPhone != null) {
                return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
            }
        }
        if (!Objects.equals(currentAccount.getEmail(), request.getEmail())) {
            Account detailAccountByEmail = accountRepository.detailAccountByEmail(request.getEmail());
            if (detailAccountByEmail != null) {
                return new ResponseEntity<>("2", HttpStatus.BAD_REQUEST);
            }
        }

        if (currentAccount.getStatus() != Integer.parseInt(request.getStatusAccount())) {
            if (countedAccountByRoleAdmin == 1 && isCurrentAdmin) {
                return new ResponseEntity<>("3", HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>("0", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> checkDeleteAccount(AccountRequest request) {
        Integer countedAccountByRoleAdmin = authenticationRepository.countAccountByRoleAdmin();
        boolean isCurrentAdmin = authenticationRepository.isAdminAccount(Long.parseLong(request.getIdAddAccount()));
        if (countedAccountByRoleAdmin == 1 && isCurrentAdmin) {
            return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("0", HttpStatus.OK);
    }
}