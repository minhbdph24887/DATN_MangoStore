package com.datn.datn_mangostore.controller;

import com.datn.datn_mangostore.bean.*;
import com.datn.datn_mangostore.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/mangostore/admin/")
public class ProductAdminController {
    private final ProductService productService;
    private final MaterialService materialService;
    private final OriginService originService;
    private final SizeService sizeService;
    private final ColorService colorService;
    private final ProductDetailService productDetailService;
    private final CategoryService categoryService;

    public ProductAdminController(ProductService productService,
                                  MaterialService materialService,
                                  OriginService originService,
                                  SizeService sizeService,
                                  ColorService colorService,
                                  ProductDetailService productDetailService,
                                  CategoryService categoryService) {
        this.productService = productService;
        this.materialService = materialService;
        this.originService = originService;
        this.sizeService = sizeService;
        this.colorService = colorService;
        this.productDetailService = productDetailService;
        this.categoryService = categoryService;

    }

    @GetMapping(value = "product")
    public String indexProductAdmin(Model model,
                                    HttpSession session,
                                    @Param("keyword") String keyword) {
        return productService.indexProductAdmin(model, session, keyword);
    }

    @GetMapping(value = "product/create")
    public String createProductAdmin(Model model,
                                     HttpSession session) {
        return productService.createProductAdmin(model, session);
    }


    @GetMapping(value = "product/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long idProduct) {
        return productService.deleteProduct(idProduct);
    }


    @GetMapping(value = "product/restore/{id}")
    public String restoreProduct(@PathVariable("id") Long idProductDetail) {
        return productService.restoreProduct(idProductDetail);
    }


    @GetMapping("/product-detail")
    public String indexProductDetail(Model model,
                                     HttpSession session,
                                     @RequestParam(name = "keyword", required = false) String keyword,
                                     @RequestParam(name = "materialId", required = false) String materialId,
                                     @RequestParam(name = "sizeId", required = false) String sizeId,
                                     @RequestParam(name = "colorId", required = false) String colorId,
                                     @RequestParam(name = "originId", required = false) String originId,
                                     @RequestParam(name = "categoryId", required = false) String categoryId,
                                     @RequestParam(name = "sortBy", required = false) String sortBy,
                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productDetailService.indexProductDetail(model, session, keyword, materialId, sizeId, colorId, originId, categoryId, sortBy, pageable);
    }


    @GetMapping(value = "product/detail/{id}")
    public String detailProduct(Model model,
                                HttpSession session,
                                @PathVariable("id") Long idProduct) {
        return productService.detailProduct(model, session, idProduct);
    }

    @GetMapping(value = "product-detail/create")
    public String viewCreateProductDetail(Model model,
                                          HttpSession session) {
        return productDetailService.viewCreateProductDetail(model, session);
    }

    @GetMapping(value = "product-detail/edit/{id}")
    public String editProductDetail(@PathVariable("id") Long idProductDetail,
                                    Model model,
                                    HttpSession session) {
        return productDetailService.editProductDetail(idProductDetail, model, session);
    }

    @PostMapping(value = "product-detail/update")
    public String updateProductDetail(@Valid ProductDetail editProductDetail,
                                      BindingResult result,
                                      HttpSession session) {
        return productDetailService.updateProductDetail(editProductDetail, result, session);
    }

    @GetMapping(value = "product-detail/delete/{id}")
    public String deleteProductDetail(@PathVariable("id") Long idProductDetail) {
        return productDetailService.deleteProductDetail(idProductDetail);
    }


    @GetMapping(value = "product-detail/restore/{id}")
    public String restoreProductDetail(@PathVariable("id") Long idProductDetail) {
        return productDetailService.restoreProductDetail(idProductDetail);
    }

    @GetMapping(value = "category")
    public String indexCategory(Model model,
                                HttpSession session,
                                @Param("keyword") String keyword) {
        return categoryService.indexCategory(model, session, keyword);
    }

    @PostMapping(value = "category/add")
    public String addCategory(@Valid Category addCategory,
                              BindingResult result,
                              HttpSession session) {
        return categoryService.addCategory(addCategory, result, session);
    }

    @GetMapping(value = "category/detail/{id}")
    public String detailCategory(Model model,
                                 HttpSession session,
                                 @PathVariable("id") Long idCategory) {
        return categoryService.detailCategory(model, session, idCategory);
    }

    @PostMapping(value = "category/update")
    public String updateCategory(@Valid Category category,
                                 BindingResult result,
                                 HttpSession session) {
        return categoryService.updateCategory(result, session, category);
    }

    @GetMapping(value = "category/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long idCategory) {
        return categoryService.deleteCategory(idCategory);
    }

    @GetMapping(value = "category/restore/{id}")
    public String restoreCategory(@PathVariable("id") Long idCategory) {
        return categoryService.restoreCategory(idCategory);
    }

    @GetMapping(value = "material")
    public String indexMaterial(Model model,
                                HttpSession session,
                                @Param("keyword") String keyword) {
        return materialService.indexMaterial(model, session, keyword);
    }

    @PostMapping(value = "material/add")
    public String addMaterial(@Valid Material addMaterial,
                              BindingResult result,
                              HttpSession session) {
        return materialService.addMaterial(addMaterial, result, session);
    }

    @GetMapping(value = "material/detail/{id}")
    public String detailMaterial(Model model,
                                 HttpSession session,
                                 @PathVariable("id") Long idMaterial) {
        return materialService.detailMaterial(model, session, idMaterial);
    }

    @PostMapping(value = "material/update")
    public String updateMaterial(@Valid Material material,
                                 BindingResult result,
                                 HttpSession session) {
        return materialService.updateMaterial(result, session, material);
    }

    @GetMapping(value = "material/delete/{id}")
    public String deleteMaterial(@PathVariable("id") Long idMaterial) {
        return materialService.deleteMaterial(idMaterial);
    }

    @GetMapping(value = "material/restore/{id}")
    public String restoreMaterial(@PathVariable("id") Long idMaterial) {
        return materialService.restoreMaterial(idMaterial);
    }


    @GetMapping(value = "origin")
    public String indexOrigin(Model model,
                              HttpSession session,
                              @Param("keyword") String keyword) {
        return originService.indexOrigin(model, session, keyword);
    }

    @PostMapping(value = "origin/add")
    public String addOrigin(@Valid Origin addOrigin,
                            BindingResult result,
                            HttpSession session) {
        return originService.addOrigin(addOrigin, result, session);
    }

    @GetMapping(value = "origin/detail/{id}")
    public String detailOrigin(Model model,
                               HttpSession session,
                               @PathVariable("id") Long idOrigin) {
        return originService.detailOrigin(model, session, idOrigin);
    }

    @PostMapping(value = "origin/update")
    public String updateOrigin(@Valid Origin origin,
                               BindingResult result,
                               HttpSession session) {
        return originService.updateOrigin(result, session, origin);
    }

    @GetMapping(value = "origin/delete/{id}")
    public String deleteOrigin(@PathVariable("id") Long idOrigin) {
        return originService.deleteOrigin(idOrigin);
    }

    @GetMapping(value = "origin/restore/{id}")
    public String restoreOrigin(@PathVariable("id") Long idOrigin) {
        return originService.restoreOrigin(idOrigin);
    }


    @GetMapping(value = "size")
    public String indexSize(Model model,
                            HttpSession session,
                            @Param("keyword") String keyword) {
        return sizeService.indexSize(model, session, keyword);
    }

    @PostMapping(value = "size/add")
    public String addSize(@Valid Size addSize,
                          BindingResult result,
                          HttpSession session) {
        return sizeService.addSize(addSize, result, session);
    }

    @GetMapping(value = "size/detail/{id}")
    public String detailSize(Model model,
                             HttpSession session,
                             @PathVariable("id") Long idSize) {
        return sizeService.detailSize(model, session, idSize);
    }

    @PostMapping(value = "size/update")
    public String updateSize(@Valid Size size,
                             BindingResult result,
                             HttpSession session) {
        return sizeService.updateSize(result, session, size);
    }

    @GetMapping(value = "size/delete/{id}")
    public String deleteSize(@PathVariable("id") Long idSize) {
        return sizeService.deleteSize(idSize);
    }

    @GetMapping(value = "size/restore/{id}")
    public String restoreSize(@PathVariable("id") Long idSize) {
        return sizeService.restoreSize(idSize);
    }

    @GetMapping(value = "color")
    public String indexColor(Model model,
                             HttpSession session,
                             @Param("keyword") String keyword) {
        return colorService.indexColor(model, session, keyword);
    }

    @PostMapping(value = "color/add")
    public String addColor(@Valid Color addColor,
                           BindingResult result,
                           HttpSession session) {
        return colorService.addColor(addColor, result, session);
    }

    @GetMapping(value = "color/detail/{id}")
    public String detailColor(Model model,
                              HttpSession session,
                              @PathVariable("id") Long idColor) {
        return colorService.detailColor(model, session, idColor);
    }

    @PostMapping(value = "color/update")
    public String updateColor(@Valid Color color,
                              BindingResult result,
                              HttpSession session) {
        return colorService.updateColor(result, session, color);
    }

    @GetMapping(value = "color/delete/{id}")
    public String deleteColor(@PathVariable("id") Long idColor) {
        return colorService.deleteColor(idColor);
    }

    @GetMapping(value = "color/restore/{id}")
    public String restoreColor(@PathVariable("id") Long idColor) {
        return colorService.restoreColor(idColor);
    }


}
