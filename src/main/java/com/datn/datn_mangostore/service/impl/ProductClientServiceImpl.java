package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.*;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.reponse.PriceRange;
import com.datn.datn_mangostore.repository.*;
import com.datn.datn_mangostore.request.AddToCartRequest;
import com.datn.datn_mangostore.request.AddToFavouriteRequest;
import com.datn.datn_mangostore.request.QuantityRequest;
import com.datn.datn_mangostore.service.ProductClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductClientServiceImpl implements ProductClientService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final ProductDetailRepository productDetailRepository;
    private final Gender gender;
    private final ImagesRepository imagesRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartDetailRepository shoppingCartDetailRepository;
    private final FavouriteRepository favouriteRepository;
    private final FavouriteDetailRepository favouriteDetailRepository;

    public ProductClientServiceImpl(AccountRepository accountRepository,
                                    RoleRepository roleRepository,
                                    ProductDetailRepository productDetailRepository,
                                    Gender gender,
                                    ImagesRepository imagesRepository,
                                    ColorRepository colorRepository,
                                    SizeRepository sizeRepository,
                                    ShoppingCartRepository shoppingCartRepository,
                                    ShoppingCartDetailRepository shoppingCartDetailRepository,
                                    FavouriteRepository favouriteRepository,
                                    FavouriteDetailRepository favouriteDetailRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.productDetailRepository = productDetailRepository;
        this.gender = gender;
        this.imagesRepository = imagesRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartDetailRepository = shoppingCartDetailRepository;
        this.favouriteRepository = favouriteRepository;
        this.favouriteDetailRepository = favouriteDetailRepository;
    }

    @Override
    public String indexProductClient(Model model,
                                     HttpSession session,
                                     String sortDirection,
                                     Integer pageNo) {
        String email = (String) session.getAttribute("loginEmail");
        if (email != null) {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);

            Role detailRoleByEmail = roleRepository.getRoleByEmail(email);
            if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                model.addAttribute("checkAuthentication", detailRoleByEmail);
            }
        }

        Page<ProductDetail> itemsProductDetail;
        if ("LowToHigh".equals(sortDirection)) {
            itemsProductDetail = productDetailRepository.sortProductDetailLowToHigh(PageRequest.of(pageNo - 1, 8));
            model.addAttribute("sortDirection", "LowToHigh");
        } else if ("HighToLow".equals(sortDirection)) {
            itemsProductDetail = productDetailRepository.sortProductDetailHighToLow(PageRequest.of(pageNo - 1, 8));
            model.addAttribute("sortDirection", "HighToLow");
        } else {
            itemsProductDetail = productDetailRepository.getAllProductDetailByIdProduct(PageRequest.of(pageNo - 1, 8));
        }
        model.addAttribute("listProductDetail", itemsProductDetail);
        model.addAttribute("totalPage", itemsProductDetail.getTotalPages());
        model.addAttribute("currentPage", pageNo);

        Map<Long, PriceRange> priceRangeMap = gender.getPriceRangMap();
        model.addAttribute("priceRangeMap", priceRangeMap);
        return "client/ProductClient/ListProductClient";
    }

    @Override
    public String detailProductClient(Long idProductDetail,
                                      Model model,
                                      HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email != null) {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);
            Role detailRoleByEmail = roleRepository.getRoleByEmail(email);
            if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                model.addAttribute("checkAuthentication", detailRoleByEmail);
            }
        }

        ProductDetail productDetail = productDetailRepository.findById(idProductDetail).orElse(null);
        model.addAttribute("detailProductClient", productDetail);

        assert productDetail != null;
        List<Images> listImagesByProduct = imagesRepository.findAllImagesByProduct(productDetail.getProduct().getId());
        model.addAttribute("listImagesByProduct", listImagesByProduct);

        Map<Long, PriceRange> priceRangeMap = gender.getPriceRangMap();
        model.addAttribute("priceRangeMap", priceRangeMap);

        List<Color> itemsColor = colorRepository.findAll();
        model.addAttribute("listColor", itemsColor);

        List<Size> itemsSize = sizeRepository.findAll();
        model.addAttribute("listSize", itemsSize);

        List<ProductDetail> getAllProductDetailByCategory = productDetailRepository.findAllByIdCategory(productDetail.getCategory().getId());
        model.addAttribute("listProductDetailByCategory", getAllProductDetailByCategory);
        return "client/ProductClient/DetailProductClient";
    }

    @Override
    public ResponseEntity<?> quantityProductDetail(QuantityRequest request) {
        ProductDetail detailProduct = productDetailRepository.getQuantityProductDetail(request.getIdProduct(), request.getIdSize(), request.getIdColor());
        if (detailProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Sản phẩm không tồn tại"));
        } else if (detailProduct.getQuantity() == 0) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Sản phẩm đã hết");
            response.put("price", detailProduct.getPrice());
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("quantity", detailProduct.getQuantity());
            response.put("price", detailProduct.getPrice());
            return ResponseEntity.ok(response);
        }
    }

    @Override
    public boolean addToCart(AddToCartRequest request, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return false;
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            ProductDetail detailProduct = productDetailRepository.getQuantityProductDetail(request.getIdProduct(), request.getIdSize(), request.getIdColor());
            ShoppingCart detailShoppingCart = shoppingCartRepository.findByIdAccount(detailAccount.getId());
            List<ShoppingCartDetail> allShoppingCartDetails = shoppingCartDetailRepository.getAllShoppingCart(detailShoppingCart.getId());

            ShoppingCartDetail existingShoppingCartDetail = allShoppingCartDetails.stream()
                    .filter(shoppingCartDetail -> shoppingCartDetail.getProductDetail().getId().equals(detailProduct.getId()))
                    .findFirst()
                    .orElse(null);

            if (existingShoppingCartDetail == null) {
                ShoppingCartDetail newShoppingCartDetail = new ShoppingCartDetail();
                newShoppingCartDetail.setShoppingCart(detailShoppingCart);
                newShoppingCartDetail.setProductDetail(detailProduct);
                newShoppingCartDetail.setQuantity(request.getQuantity());
                newShoppingCartDetail.setPrice(detailProduct.getPrice());
                newShoppingCartDetail.setCapitalSum(request.getQuantity() * detailProduct.getPrice());
                newShoppingCartDetail.setDateCreate(LocalDateTime.now());
                newShoppingCartDetail.setStatus(1);
                shoppingCartDetailRepository.save(newShoppingCartDetail);
            } else {
                Integer quantityNew = request.getQuantity() + existingShoppingCartDetail.getQuantity();
                existingShoppingCartDetail.setQuantity(quantityNew);
                existingShoppingCartDetail.setCapitalSum(quantityNew * existingShoppingCartDetail.getPrice());
                existingShoppingCartDetail.setDateCreate(LocalDateTime.now());
                shoppingCartDetailRepository.save(existingShoppingCartDetail);
            }
            gender.updateTotalShoppingCart(detailShoppingCart);
            return true;
        }
    }

    @Override
    public boolean addToFavourite(AddToFavouriteRequest request, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        assert email != null;
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        ProductDetail detailProduct = productDetailRepository.getQuantityProductDetail(request.getIdProduct(), request.getIdSize(), request.getIdColor());
        Favourite detailFavourite = favouriteRepository.findByIdAccount(detailAccount.getId());
        List<FavouriteDetail> allFavouriteDetails = favouriteDetailRepository.getAllFavourite(detailFavourite.getId());

        FavouriteDetail existingFavouriteDetail = allFavouriteDetails.stream()
                .filter(favouriteDetail -> favouriteDetail.getProductDetail().getId().equals(detailProduct.getId()))
                .findFirst()
                .orElse(null);

        if (existingFavouriteDetail == null) {
            FavouriteDetail newFavouriteDetail = new FavouriteDetail();
            newFavouriteDetail.setFavourite(detailFavourite);
            newFavouriteDetail.setProductDetail(detailProduct);
            newFavouriteDetail.setDateCreate(LocalDateTime.now());
            newFavouriteDetail.setStatus(1);
            favouriteDetailRepository.save(newFavouriteDetail);
            return true;
        } else {
            return false;
        }
    }
}