package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Authentication;
import com.datn.datn_mangostore.bean.Rank;
import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.AuthenticationRepository;
import com.datn.datn_mangostore.repository.RankRepository;
import com.datn.datn_mangostore.repository.RoleRepository;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final RankRepository rankRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationRepository authenticationRepository;

    public ProfileServiceImpl(AccountRepository accountRepository,
                              RoleRepository roleRepository,
                              RankRepository rankRepository,
                              PasswordEncoder encoder,
                              AuthenticationRepository authenticationRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.rankRepository = rankRepository;
        this.encoder = encoder;
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    public String getAllStaffByStatus1(Model model,
                                       HttpSession session) {
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

                List<Account> itemsManager = accountRepository.getAllAccountByRoleManagerAndStatus1();
                model.addAttribute("listManager", itemsManager);

                List<Account> itemsStaff = accountRepository.getAllAccountByRoleStaffAndStatus1();
                model.addAttribute("listStaff", itemsStaff);

                List<Account> itemsManagerInactive = accountRepository.getAllAccountByRoleManagerAndStatus0();
                model.addAttribute("listManagerInactive", itemsManagerInactive);

                List<Account> itemsStaffInactive = accountRepository.getAllAccountByRoleStaffAndStatus0();
                model.addAttribute("listStaffInactive", itemsStaffInactive);

                Role detailRole = roleRepository.getRoleByEmail(email);
                if (detailRole.getName().equals("ADMIN")) {
                    model.addAttribute("checkIndexAccount", true);
                    model.addAttribute("checkMenuAdmin", true);
                    model.addAttribute("addProfile", new Account());
                } else {
                    model.addAttribute("checkIndexAccount", false);
                    model.addAttribute("checkMenuAdmin", false);
                }
                return "admin/staff/IndexStaff";
            }
        }
    }

    @Override
    public String restoreStaff(Long idAccount) {
        Account detailAccount = accountRepository.findById(idAccount).orElse(null);
        detailAccount.setStatus(1);
        accountRepository.save(detailAccount);
        return "redirect:/mangostore/admin/list-staff";
    }

    @Override
    public String restorClient(Long idAccount) {
        Account detailAccount = accountRepository.findById(idAccount).orElse(null);
        detailAccount.setStatus(1);
        accountRepository.save(detailAccount);
        return "redirect:/mangostore/admin/list-client";
    }

    @Override
    public String detailStaff(Model model,
                              HttpSession session,
                              Long idAccount) {
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

                Account detailProfile = accountRepository.findById(idAccount).orElse(null);
                model.addAttribute("detailProfile", detailProfile);

                String accountRole = roleRepository.getRoleByEmail(detailProfile.getEmail()).getName();
                model.addAttribute("accountRole", accountRole);

                Role detailRole = roleRepository.getRoleByEmail(email);
                if (detailRole.getName().equals("ADMIN")) {
                    model.addAttribute("checkDetailAccount", true);
                    model.addAttribute("checkMenuAdmin", true);
                } else {
                    model.addAttribute("checkDetailAccount", false);
                    model.addAttribute("checkMenuAdmin", false);
                }
                return "admin/staff/DetailStaff";
            }
        }
    }

    @Override
    public String detailViewStaff(Model model,
                                  HttpSession session) {
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

                Account detailProfile = accountRepository.findById(detailAccount.getId()).orElse(null);
                model.addAttribute("detailProfile", detailProfile);

                String accountRole = roleRepository.getRoleByEmail(detailProfile.getEmail()).getName();
                model.addAttribute("accountRole", accountRole);

                Role detailRole = roleRepository.getRoleByEmail(email);
                if (detailRole.getName().equals("ADMIN")) {
                    model.addAttribute("checkDetailAccount", true);
                    model.addAttribute("checkMenuAdmin", true);
                } else {
                    model.addAttribute("checkDetailAccount", false);
                    model.addAttribute("checkMenuAdmin", false);
                }
            }
            return "admin/staff/DetailStaff";
        }
    }

    @Override
    public String detailclient(Model model, HttpSession session, Long idAccount) {
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

                Account detailProfile = accountRepository.findById(idAccount).orElse(null);
                model.addAttribute("detailProfile", detailProfile);

                String accountRole = roleRepository.getRoleByEmail(detailProfile.getEmail()).getName();
                model.addAttribute("accountRole", accountRole);

                Role detailRole = roleRepository.getRoleByEmail(email);
                if (detailRole.getName().equals("ADMIN")) {
                    model.addAttribute("checkDetailAccount", true);
                    model.addAttribute("checkMenuAdmin", true);
                } else {
                    model.addAttribute("checkDetailAccount", false);
                    model.addAttribute("checkMenuAdmin", false);
                }
                return "admin/client/DetailClient";
            }
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
    public String addAccountStaff(BindingResult result, Account addProfile) {
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
    public String getAllAccountByClient(Model model, HttpSession session) {

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

                List<Account> itemsAccount = accountRepository.getAllAccountByClient();
                model.addAttribute("listClient", itemsAccount);

                List<Account> itemsAccountInactive = accountRepository.getAllAccountByClient0();
                model.addAttribute("listClientInactive", itemsAccountInactive);


                Role detailRole = roleRepository.getRoleByEmail(email);
                if (detailRole.getName().equals("ADMIN")) {
                    model.addAttribute("checkIndexAccount", true);
                    model.addAttribute("checkMenuAdmin", true);
                    model.addAttribute("addClient", new Account());
                } else {
                    model.addAttribute("checkIndexAccount", false);
                    model.addAttribute("checkMenuAdmin", false);
                }
                return "admin/client/IndexClient";
            }
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
        assert currentAccount != null;

        if (!request.getNumberPhone().equals(currentAccount.getNumberPhone())) {
            Account detailAccountByNumberPhone = accountRepository.findAccountByNumberPhone(request.getNumberPhone());
            if (detailAccountByNumberPhone != null) {
                return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
            }
        }
        if (!request.getEmail().equals(currentAccount.getEmail())) {
            Account detailAccountByEmail = accountRepository.detailAccountByEmail(request.getEmail());
            if (detailAccountByEmail != null) {
                return new ResponseEntity<>("2", HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>("0", HttpStatus.OK);
    }

// Override
//    public ResponseEntity<String> checkUpdateAccountClient(DataRequest request) {
//        Account currentAccount = accountRepository.findById(Long.parseLong(request.getIdUpdate())).orElse(null);
//        assert currentAccount != null;
//
//        if (!request.getNumberPhone().equals(currentAccount.getNumberPhone())) {
//            Account detailAccountByNumberPhone = accountRepository.findAccountByNumberPhone(request.getNumberPhone());
//            if (detailAccountByNumberPhone != null) {
//                return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
//            }
//        }
//        if (!request.getEmail().equals(currentAccount.getEmail())) {
//            Account detailAccountByEmail = accountRepository.detailAccountByEmail(request.getEmail());
//            if (detailAccountByEmail != null) {
//                return new ResponseEntity<>("2", HttpStatus.BAD_REQUEST);
//            }
//        }
//
//        return new ResponseEntity<>("0", HttpStatus.OK);
//    }


    @Override
    public ResponseEntity<String> checkDeleteAccount(DataRequest request) {
        Account currentAccount = accountRepository.findById(Long.parseLong(request.getIdDelete())).orElse(null);
       // assert currentAccount != null;

//        if (!request.getIdDelete().equals(currentAccount.getId())) {
//            Account iddelete = accountRepository.detailAccountById(Long.parseLong(request.getIdDelete()));
//            if (iddelete != null) {
//                return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
//            }
//        }
        return new ResponseEntity<>("0", HttpStatus.OK);
    }
}
