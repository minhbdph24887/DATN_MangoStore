package com.datn.datn_mangostore.controller;

import com.datn.datn_mangostore.service.ProductClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/mangostore/")
public class ProductClientController {
    private final ProductClientService productClientService;

    public ProductClientController(ProductClientService productClientService) {
        this.productClientService = productClientService;
    }

    @GetMapping(value = "product")
    public String viewIndexProductClient(Model model,
                                         HttpSession session,
                                         @Param("sortDirection") String sortDirection,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        return productClientService.indexProductClient(model, session, sortDirection, pageNo);
    }

    @GetMapping(value = "product/detail/{id}")
    public String productDetailClient(@PathVariable("id") Long idProductDetail,
                                      Model model,
                                      HttpSession session) {
        return productClientService.detailProductClient(idProductDetail, model, session);
    }
}
