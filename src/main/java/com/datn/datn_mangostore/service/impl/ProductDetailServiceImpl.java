package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.*;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.*;
import com.datn.datn_mangostore.request.CreateProductRequest;
import com.datn.datn_mangostore.request.ProductDetailRequest;
import com.datn.datn_mangostore.request.RestoreProductDetailRequest;
import com.datn.datn_mangostore.request.VariantRequest;
import com.datn.datn_mangostore.service.ProductDetailService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {
    private final AccountRepository accountRepository;
    private final Gender gender;
    private final MaterialRepository materialRepository;
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;
    private final OriginRepository originRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;

    public ProductDetailServiceImpl(AccountRepository accountRepository,
                                    Gender gender,
                                    MaterialRepository materialRepository,
                                    SizeRepository sizeRepository,
                                    ColorRepository colorRepository,
                                    OriginRepository originRepository,
                                    CategoryRepository categoryRepository,
                                    ProductRepository productRepository,
                                    ProductDetailRepository productDetailRepository) {
        this.accountRepository = accountRepository;
        this.gender = gender;
        this.materialRepository = materialRepository;
        this.sizeRepository = sizeRepository;
        this.colorRepository = colorRepository;
        this.originRepository = originRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productDetailRepository = productDetailRepository;
    }

    public List<ProductDetail> searchAndFilter(String keyword,
                                               String materialId,
                                               String sizeId,
                                               String colorId,
                                               String originId,
                                               String categoryId,
                                               String sortBy) {
        Specification<ProductDetail> spec = Specification.where(ProductDetailSpecifications.hasKeyword(keyword))
                .and(ProductDetailSpecifications.hasMaterialId(materialId))
                .and(ProductDetailSpecifications.hasSizeId(sizeId))
                .and(ProductDetailSpecifications.hasColorId(colorId))
                .and(ProductDetailSpecifications.hasOriginId(originId))
                .and(ProductDetailSpecifications.hasCategoryId(categoryId))
                .and(ProductDetailSpecifications.isActive());

        if (sortBy != null && !sortBy.isEmpty()) {
            Sort sort = switch (sortBy) {
                case "asc" -> Sort.by("price").ascending();
                case "desc" -> Sort.by("price").descending();
                default -> Sort.unsorted();
            };
            return productDetailRepository.findAll(spec, sort);
        } else {
            Sort sortById = Sort.by("id").descending();
            return productDetailRepository.findAll(spec, sortById);
        }
    }


    @Override
    public String indexProductDetail(Model model,
                                     HttpSession session,
                                     String keyword,
                                     String materialId,
                                     String sizeId,
                                     String colorId,
                                     String originId,
                                     String categoryId,
                                     String sortBy) {
        Account detailAccount = gender.checkMenuAdmin(model, session, "Admin | Trang chủ sản phẩm");
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<ProductDetail> itemsProductDetailInactive = productDetailRepository.getAllProductDetailByStatus0();
            model.addAttribute("listProductDetailInactive", itemsProductDetailInactive);

            List<ProductDetail> itemsProductDetail = searchAndFilter(keyword, materialId, sizeId, colorId, originId, categoryId, sortBy);

            model.addAttribute("listProductDetail", itemsProductDetail);
            model.addAttribute("keyword", keyword);

            List<Material> materials = materialRepository.getAllMaterialByStatus1();
            List<Size> sizes = sizeRepository.getAllSizeByStatus1();
            List<Color> colors = colorRepository.getAllColorByStatus1();
            List<Origin> origins = originRepository.getAllOriginByStatus1();
            List<Category> categories = categoryRepository.getAllCategoryByStatus1();

            model.addAttribute("materials", materials);
            model.addAttribute("sizes", sizes);
            model.addAttribute("colors", colors);
            model.addAttribute("origins", origins);
            model.addAttribute("categories", categories);
            return "admin/ProductDetail/IndexProductDetail";
        }
    }


    @Override
    public String viewCreateProductDetail(Model model,
                                          HttpSession session) {
        Account detailAccount = gender.checkMenuAdmin(model, session, "Admin | Tạo sản phẩm");
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Product> itemsProduct = productRepository.getAllProductByStatus1();
            model.addAttribute("listProduct", itemsProduct);

            List<Size> itemsSize = sizeRepository.getAllSizeByStatus1();
            model.addAttribute("listSize", itemsSize);

            List<Color> itemsColor = colorRepository.getAllColorByStatus1();
            model.addAttribute("listColor", itemsColor);

            List<Material> itemsMaterial = materialRepository.getAllMaterialByStatus1();
            model.addAttribute("listMaterial", itemsMaterial);

            List<Origin> itemsOrigin = originRepository.getAllOriginByStatus1();
            model.addAttribute("listOrigin", itemsOrigin);

            List<Category> itemsCategory = categoryRepository.getAllCategoryByStatus1();
            model.addAttribute("listCategory", itemsCategory);

            List<ProductDetail> itemsProductDetailInactive = productDetailRepository.getAllProductDetailByStatus0();
            model.addAttribute("listProductDetailInactive", itemsProductDetailInactive);

            model.addAttribute("productDetailForm", new ProductDetailRequest());
            return "admin/ProductDetail/CreateProductDetail";
        }
    }

    @Override
    public Map<String, List<String>> saveProductDetailAPI(CreateProductRequest request,
                                                          HttpSession session,
                                                          HttpServletResponse response) throws IOException {
        String email = (String) session.getAttribute("loginEmail");
        List<String> addedProductDetails = new ArrayList<>();
        Map<String, List<String>> result = new HashMap<>();

        if (email == null) {
            response.sendRedirect("/mangostore/home");
            return result;
        } else {
            Account account = accountRepository.detailAccountByEmail(email);
            Optional<Product> product = productRepository.findById(request.getIdProduct());
            Optional<Material> material = materialRepository.findById(request.getIdMaterial());
            Optional<Origin> origin = originRepository.findById(request.getIdOrigin());
            Optional<Category> category = categoryRepository.findById(request.getIdCategory());

            for (VariantRequest items : request.getVariantRequests()) {
                Size size = sizeRepository.findByName(items.getSize());
                Color color = colorRepository.findByName(items.getColor());

                ProductDetail existingDetail = productDetailExists(request.getIdProduct(),
                        request.getIdMaterial(),
                        size.getId(),
                        color.getId(),
                        request.getIdOrigin(),
                        request.getIdCategory());
                if (existingDetail == null) {
                    ProductDetail productDetail = new ProductDetail();
                    productDetail.setDescribe(request.getDescribe());
                    productDetail.setNameUserCreate(account.getFullName());
                    productDetail.setNameUserUpdate(account.getFullName());
                    productDetail.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
                    productDetail.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
                    productDetail.setImportPrice(items.getImportPrice());
                    productDetail.setPrice(items.getPrice());
                    productDetail.setStatus(1);
                    productDetail.setQuantity(items.getQuantity());
                    productDetail.setProduct(product.get());
                    productDetail.setMaterial(material.get());
                    productDetail.setOrigin(origin.get());
                    productDetail.setCategory(category.get());
                    productDetail.setSize(size);
                    productDetail.setColor(color);
                    productDetail.setProductStatus(1);
                    productDetailRepository.save(productDetail);
                    addedProductDetails.add(productDetail.getDescribe());
                }
            }
        }
        result.put("added", addedProductDetails);
        return result;
    }

    private ProductDetail productDetailExists(Long productId,
                                              Long materialId,
                                              Long sizeId,
                                              Long colorId,
                                              Long originId,
                                              Long categoryId) {
        return productDetailRepository.findExistingProductDetail(productId,
                materialId,
                sizeId,
                colorId,
                originId,
                categoryId);
    }


    @Override
    public String addProductDetail(ProductDetailRequest productDetailForm,
                                   BindingResult result,
                                   HttpSession session,
                                   Model model) {
        List<Product> products = productRepository.getAllProductByStatus1();
        model.addAttribute("listProduct", products);

        List<Size> sizes = sizeRepository.getAllSizeByStatus1();
        model.addAttribute("listSize", sizes);

        List<Color> colors = colorRepository.getAllColorByStatus1();
        model.addAttribute("listColor", colors);

        List<Material> materials = materialRepository.getAllMaterialByStatus1();
        model.addAttribute("listMaterial", materials);

        model.addAttribute("productDetailForm", new ProductDetailRequest());

        Product product = productRepository.findByMa(productDetailForm.getIdProduct());
        Material material = materialRepository.findByMa(productDetailForm.getMaterial());

        if (productDetailForm.getProductVariants() == null) {
            productDetailForm.setProductVariants(new ArrayList<>());
        }

        for (Long sizeId : productDetailForm.getIdSizes()) {
            for (Long colorId : productDetailForm.getIdColors()) {
                Size size = sizeRepository.findById(sizeId).orElse(null);
                Color color = colorRepository.findById(colorId).orElse(null);

                if (size != null && color != null) {
                    ProductDetail productDetail = new ProductDetail();
                    productDetail.setSize(size);
                    productDetail.setColor(color);
                    productDetail.setProduct(product);
                    productDetail.setMaterial(material);
                    productDetail.setQuantity(productDetailForm.getQuantity());
                    productDetail.setImportPrice(productDetail.getImportPrice());
                    productDetail.setPrice(productDetail.getPrice());
                    productDetailRepository.save(productDetail);
                    productDetailForm.getProductVariants().add(productDetail);
                }
            }
        }
        model.addAttribute("variants", productDetailForm.getProductVariants());
        return "admin/productDetail/CreateProductDetail";
    }

    @Override
    public String editProductDetail(Long idProductDetail,
                                    Model model,
                                    HttpSession session) {
        Account detailAccount = gender.checkMenuAdmin(model, session, "Admin | Cập nhật sản phẩm");
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            ProductDetail productDetail = productDetailRepository.findById(idProductDetail).orElse(null);
            model.addAttribute("editProductDetail", productDetail);

            List<ProductDetail> itemsProductDetailInactive = productDetailRepository.getAllProductDetailByStatus0();
            model.addAttribute("listProductDetailInactive", itemsProductDetailInactive);
            return "admin/ProductDetail/EditProductDetail";
        }
    }

    @Override
    public String updateProductDetail(ProductDetail editProductDetail,
                                      BindingResult result,
                                      HttpSession session) {
        ProductDetail productDetail = productDetailRepository.findById(editProductDetail.getId()).orElse(null);
        assert productDetail != null;
        productDetail.setDescribe(editProductDetail.getDescribe());
        productDetail.setQuantity(editProductDetail.getQuantity());
        productDetail.setImportPrice(editProductDetail.getImportPrice());
        productDetail.setPrice(editProductDetail.getPrice());

        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        productDetail.setNameUserUpdate(detailAccount.getFullName());
        productDetail.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));

        if (editProductDetail.getQuantity() == 0) {
            productDetail.setProductStatus(0);
            productDetail.setStatus(0);
        } else {
            productDetail.setProductStatus(1);
        }

        productDetailRepository.save(productDetail);
        return "redirect:/mangostore/admin/product-detail";
    }

    @Override
    public String deleteProductDetail(Long idProductDetail) {
        ProductDetail productDetail = productDetailRepository.findById(idProductDetail).orElse(null);
        if (productDetail != null) {
            productDetail.setStatus(0);
            productDetailRepository.save(productDetail);
            return "redirect:/mangostore/admin/product-detail";
        } else {
            return "redirect:/mangostore/admin/product-detail";
        }
    }

    @Override
    public String restoreProductDetail(Long aLong) {
        ProductDetail productDetail = productDetailRepository.findById(aLong).orElse(null);
        assert productDetail != null;
        productDetail.setStatus(1);
        productDetailRepository.save(productDetail);
        return "redirect:/mangostore/admin/product-detail";
    }

    @Override
    public boolean restoreProductDetailAPI(RestoreProductDetailRequest request) {
        ProductDetail productDetail = productDetailRepository.findById(request.getIdProductDetail()).orElse(null);
        assert productDetail != null;
        productDetail.setQuantity(request.getQuantity());
        productDetail.setProductStatus(1);
        productDetail.setStatus(1);
        productDetailRepository.save(productDetail);
        return true;
    }
}
