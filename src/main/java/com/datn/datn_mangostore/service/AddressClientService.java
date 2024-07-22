package com.datn.datn_mangostore.service;

import com.datn.datn_mangostore.bean.AddressClient;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface AddressClientService {
    String indexAddressClient(Model model,
                              HttpSession session,
                              String keyword);

    String addAddressClient(AddressClient addAddressClient,
                            BindingResult result,
                            HttpSession session);

    String editAddressClient(Long idAddressClient,
                             Model model,
                             HttpSession session);

    String updateAddressClient(AddressClient editAddressClient,
                               BindingResult result);

    String deleteAddressClient(Long idAddressClient);

    String restoreAddressClient(Long idAddressClient);
}
