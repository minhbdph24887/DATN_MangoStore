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
                                     String keyword,
                                     String sortDirection,
                                     Integer pageNo) {
        Account detailAccount = gender.checkMenuClient(model, session);
        assert detailAccount != null;
        Page<ProductDetail> itemsProductDetail;
        if (keyword != null && !keyword.isEmpty()) {
            if ("LowToHigh".equals(sortDirection)) {
                itemsProductDetail = productDetailRepository.sortProductDetailLowToHigh(PageRequest.of(pageNo - 1, 8), keyword);
                model.addAttribute("sortDirection", "LowToHigh");
            } else if ("HighToLow".equals(sortDirection)) {
                itemsProductDetail = productDetailRepository.sortProductDetailHighToLow(PageRequest.of(pageNo - 1, 8), keyword);
                model.addAttribute("sortDirection", "HighToLow");
            } else {
                itemsProductDetail = productDetailRepository.searchProductDetailByNameProduct(PageRequest.of(pageNo - 1, 8), keyword);
            }
            model.addAttribute("keyword", keyword);
        } else {
            if ("LowToHigh".equals(sortDirection)) {
                itemsProductDetail = productDetailRepository.sortProductDetailLowToHigh(PageRequest.of(pageNo - 1, 8), "");
                model.addAttribute("sortDirection", "LowToHigh");
            } else if ("HighToLow".equals(sortDirection)) {
                itemsProductDetail = productDetailRepository.sortProductDetailHighToLow(PageRequest.of(pageNo - 1, 8), "");
                model.addAttribute("sortDirection", "HighToLow");
            } else {
                itemsProductDetail = productDetailRepository.getAllProductDetailByIdProduct(PageRequest.of(pageNo - 1, 8));
            }
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
        Account detailAccount = gender.checkMenuClient(model, session);
        assert detailAccount != null;
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
    public ResponseEntity<String> addToCart(AddToCartRequest request,
                                            HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return new ResponseEntity<>("1", HttpStatus.BAD_REQUEST);
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
                if (quantityNew > 50) {
                    return new ResponseEntity<>("bạn đã thêm vượt quá 50 sản phẩm/món hàng rồi", HttpStatus.BAD_REQUEST);
                }
                existingShoppingCartDetail.setQuantity(quantityNew);
                existingShoppingCartDetail.setCapitalSum(quantityNew * existingShoppingCartDetail.getPrice());
                existingShoppingCartDetail.setDateCreate(LocalDateTime.now());
                shoppingCartDetailRepository.save(existingShoppingCartDetail);
            }
            gender.updateTotalShoppingCart(detailShoppingCart);
            return new ResponseEntity<>("Thêm vào giỏ hàng thành công", HttpStatus.OK);
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