package com.datn.datn_mangostore.controller;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.AddressClient;
import com.datn.datn_mangostore.bean.Authentication;
import com.datn.datn_mangostore.bean.Role;
import com.datn.datn_mangostore.service.AddressClientService;
import com.datn.datn_mangostore.service.AuthenticationService;
import com.datn.datn_mangostore.service.ProfileService;
import com.datn.datn_mangostore.service.RoleService;
import com.datn.datn_mangostore.service.impl.ClientServiceImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Controller
@RequestMapping(value = "/mangostore/admin/")
public class ProfileController {
    private final ProfileService profileService;
    private final RoleService roleService;
    private final AuthenticationService authenticationService;
    private final AddressClientService addressClientService;

    private final ClientServiceImpl clientService;

    public ProfileController(ProfileService profileService,
                             RoleService roleService,
                             AuthenticationService authenticationService, AddressClientService addressClientService, ClientServiceImpl clientService) {
        this.profileService = profileService;
        this.roleService = roleService;
        this.authenticationService = authenticationService;
        this.addressClientService = addressClientService;
        this.clientService = clientService;
    }

    @GetMapping(value = "list-staff")
    public String viewStaff(Model model,
                            HttpSession session) {
        return profileService.getAllStaffByStatus1(model, session);
    }

    @GetMapping(value = "list-client")
    public String viewUser(Model model,
                           HttpSession session) {
        return profileService.getAllAccountByClient(model, session);
    }

    @GetMapping(value = "list-staff/restore/{id}")
    public String restoreStaff(@PathVariable("id") Long idAccount) {
        return profileService.restoreStaff(idAccount);
    }

    @GetMapping(value = "list-client/restore/{id}")
    public String restorClient(@PathVariable("id") Long idAccount) {
        return profileService.restorClient(idAccount);
    }
//    @GetMapping(value = "list-role/restore/{id}")
//    public String restoreRole(@PathVariable("id") Long idRole) {
//        return profileService.restoreRole(idAccount);
//    }

    @GetMapping(value = "list-staff/detail/{id}")
    public String detailStaff(Model model,
                              HttpSession session,
                              @PathVariable("id") Long idAccount) {
        return profileService.detailStaff(model, session, idAccount);
    }

    @GetMapping(value = "list-staff/detail-view")
    public String detailViewStaff(Model model,
                                  HttpSession session) {
        return profileService.detailViewStaff(model, session);
    }

    @GetMapping(value = "client/detail/{id}")
    public String detailClient(Model model,
                               HttpSession session,
                               @PathVariable("id") Long idAccount) {
        return profileService.detailclient(model, session, idAccount);
    }




    @PostMapping(value = "list-staff/update")
    public String updateStaff(@Valid Account account,
                              @RequestParam("newPassword") String newPassword,
                              @RequestParam("rePassword") String rePassword,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              @RequestParam("id") Long idAccount,
                              BindingResult result,
                              HttpSession session) {
        if (!Objects.equals(newPassword, rePassword)) {
            return "redirect:/mangostore/admin/list-staff/detail/" + idAccount;
        } else {
            return profileService.updateStaff(result, newPassword, imageFile, account, session);
        }

    }
    @PostMapping(value = "list-client/update")
    public String updateClient(@Valid Account account,
                              @RequestParam("newPassword") String newPassword,
                              @RequestParam("rePassword") String rePassword,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              @RequestParam("id") Long idAccount,
                              BindingResult result,
                              HttpSession session) {
        if (!Objects.equals(newPassword, rePassword)) {
            return "redirect:/mangostore/admin/list-client/detail/" + idAccount;
        } else {
            return profileService.updateClient(result, newPassword, imageFile, account, session);
        }

    }


    @GetMapping(value = "list-staff/delete/{id}")
    public String deleteStaff(@PathVariable("id") Long idAccount) {
        return profileService.deleteStaff(idAccount);
    }
    @GetMapping(value = "list-client/delete/{id}")
    public String deleteClient(@PathVariable("id") Long idAccount) {
        return profileService.deleteClient(idAccount);
    }

    @PostMapping(value = "list-client/add")
    public String addAccountClient(@Valid Account addClient,
                                   BindingResult result) {
        return profileService.addAccountClient(result, addClient);
    }

    @PostMapping(value = "add-staff")
    public String addAccountAdmin(@Valid Account addProfile,
                                  BindingResult result) {
        return profileService.addAccountStaff(result, addProfile);
    }


    @GetMapping(value = "role")
    public String viewRole(Model model,
                           HttpSession session) {
        return roleService.getAllRoleByStatus1(model, session);
    }

    @GetMapping(value = "role/restore/{id}")
    public String restoreRole(@PathVariable("id") Long idRole) {
        return roleService.restoreRole(idRole);
    }

    @GetMapping(value = "role/detail/{id}")
    public String detailRole(Model model,
                             HttpSession session,
                             @PathVariable("id") Long idRole) {
        return roleService.detailRole(model, session, idRole);
    }

    @PostMapping(value = "role/update")
    public String updateRole(@Valid Role role,
                             @RequestParam("id") Long idRole,
                             BindingResult result) {
        return roleService.updateRole(result, idRole, role);
    }

    @GetMapping(value = "role/delete/{id}")
    public String deleteRole(@PathVariable("id") Long idRole) {
        return roleService.deleteRole(idRole);
    }

    @PostMapping(value = "role/add")
    public String addRole(@Valid Role addRole,
                          BindingResult result) {
        return roleService.addRole(result, addRole);
    }

    @GetMapping(value = "authentication")
    public String viewAuthentication(Model model,
                                     HttpSession session) {
        return authenticationService.getAllRole(model, session);
    }

    @GetMapping(value = "authentication/detail/{id}")
    public String detailAuthentication(Model model,
                                       HttpSession session,
                                       @PathVariable("id") Long idAuthentication) {
        return authenticationService.detailAuthentication(model, session, idAuthentication);
    }

    @PostMapping(value = "authentication/update")
    public String updateAuthentication(Authentication updateAuthentication,
                                       @RequestParam("role") Role roleSelect) {
        return authenticationService.updateAuthentication(updateAuthentication, roleSelect);
    }

    @GetMapping(value = "address-client")
    public String indexAddressClient(Model model,
                                     HttpSession session,
                                     @Param("keyword") String keyword) {
        return addressClientService.indexAddressClient(model, session, keyword);
    }

    @PostMapping(value = "address-client/add")
    public String addAddressClient(@Valid AddressClient addAddressClient,
                                   BindingResult result,
                                   HttpSession session) {
        return addressClientService.addAddressClient(addAddressClient, result, session);
    }

    @GetMapping(value = "address-client/detail/{id}")
    public String editAddressClient(@PathVariable("id") Long idAddressClient,
                                    Model model,
                                    HttpSession session) {
        return addressClientService.editAddressClient(idAddressClient, model, session);
    }

    @PostMapping(value = "address-client/update")
    public String updateAddressClient(@Valid AddressClient editAddressClient,
                                      BindingResult result) {
        return addressClientService.updateAddressClient(editAddressClient, result);
    }

    @GetMapping(value = "address-client/delete/{id}")
    public String deleteAddressClient(@PathVariable("id") Long idAddressClient) {
        return addressClientService.deleteAddressClient(idAddressClient);
    }

    @GetMapping(value = "address-client/restore/{id}")
    public String restoreAddressClient(@PathVariable("id") Long idAddressClient) {
        return addressClientService.restoreAddressClient(idAddressClient);
    }
}
