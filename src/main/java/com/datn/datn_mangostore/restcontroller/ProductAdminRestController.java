package com.datn.datn_mangostore.restcontroller;

import com.datn.datn_mangostore.request.AddProductRequest;
import com.datn.datn_mangostore.request.CreateProductRequest;
import com.datn.datn_mangostore.request.RestoreProductDetailRequest;
import com.datn.datn_mangostore.request.UpdateProductRequest;
import com.datn.datn_mangostore.service.ImageService;
import com.datn.datn_mangostore.service.ProductDetailService;
import com.datn.datn_mangostore.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/mangostore/admin/")
public class ProductAdminRestController {
    private final ProductService productService;
    private final ProductDetailService productDetailService;
    private final ImageService imageService;

    public ProductAdminRestController(ProductService productService,
                                      ProductDetailService productDetailService,
                                      ImageService imageService) {
        this.productService = productService;
        this.productDetailService = productDetailService;
        this.imageService = imageService;
    }



    @PostMapping(value = "product/add")
    public boolean addProduct(@RequestBody AddProductRequest request,
                              HttpSession session) {
        return productService.addProductAPI(request, session);
    }
//Vinh

    @PostMapping(value = "product-detail/add")
    public Map<String, List<String>> saveProductDetailAPI(@RequestBody CreateProductRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        return productDetailService.saveProductDetailAPI(request, session, response);
    }
    //Vinh
    @PostMapping(value = "product/update")
    public boolean updateProduct(@RequestBody UpdateProductRequest request,
                                 HttpSession session) {
        return productService.updateProductAPI(request, session);
    }
//Vinh

    @GetMapping(value = "image")
    public List<String> indexProductAdmin() {
        return imageService.findAllImagesFiles();
    }


    @PostMapping(value = "product-detail/restore")
    public boolean restoreProductDetail(@RequestBody RestoreProductDetailRequest request) {
        return productDetailService.restoreProductDetailAPI(request);
    }




}
