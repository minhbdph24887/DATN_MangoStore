package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.Account;
import com.datn.datn_mangostore.bean.Images;
import com.datn.datn_mangostore.bean.Product;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.ImagesRepository;
import com.datn.datn_mangostore.repository.ProductRepository;
import com.datn.datn_mangostore.request.AddProductRequest;
import com.datn.datn_mangostore.request.UpdateProductRequest;
import com.datn.datn_mangostore.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final Gender gender;
    private final ImagesRepository imagesRepository;

    public ProductServiceImpl(AccountRepository accountRepository,
                              ProductRepository productRepository,
                              Gender gender,
                              ImagesRepository imagesRepository) {
        this.accountRepository = accountRepository;
        this.productRepository = productRepository;
        this.gender = gender;
        this.imagesRepository = imagesRepository;

    }


    @Override
    public String indexProductAdmin(Model model,
                                    HttpSession session,
                                    String keyword) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Product> itemsProduct = productRepository.getAllProductByStatus1();
            if (keyword != null) {
                itemsProduct = productRepository.searchProduct(keyword);
                model.addAttribute("keyword", keyword);
            }
            model.addAttribute("listProduct", itemsProduct);

            List<Product> itemsProductInactive = productRepository.getAllProductByStatus0();
            model.addAttribute("listProductInactive", itemsProductInactive);
            return "admin/Product/IndexProduct";
        }
    }

    @Override
    public String createProductAdmin(Model model,
                                     HttpSession session) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Product> itemsProductInactive = productRepository.getAllProductByStatus0();
            model.addAttribute("listProductInactive", itemsProductInactive);
            return "admin/Product/CreateProduct";
        }
    }

    @Override
    public boolean addProductAPI(AddProductRequest request, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        assert email != null;
        Account detailAccount = accountRepository.detailAccountByEmail(email);

        Product newProduct = new Product();
        newProduct.setCodeProduct(gender.generateCode());
        newProduct.setNameProduct(request.getNameProduct());
        newProduct.setNameUserUpdate(detailAccount.getFullName());
        newProduct.setNameUserCreate(detailAccount.getFullName());
        newProduct.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newProduct.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newProduct.setStatus(1);
        productRepository.save(newProduct);
        List<String> existingImages = imagesRepository.findAllImagesFiles();
        List<Images> addedImages = new ArrayList<>();

        for (String fileImages : request.getListImages()) {
            if (!existingImages.contains(fileImages)) {
                Images newImage = new Images();
                newImage.setCodeImages(gender.generateCode());
                newImage.setImagesFile(fileImages);
                newImage.setSaveToProduct(newProduct.getId());
                newImage.setNameUserCreate(detailAccount.getFullName());
                newImage.setNameUserUpdate(detailAccount.getFullName());
                newImage.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
                newImage.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
                newImage.setStatus(0);
                imagesRepository.save(newImage);
                addedImages.add(newImage);
            }
        }

        Images detailImages = imagesRepository.findByFileImages(request.getImagesActive());
        if (detailImages == null || existingImages.contains(request.getImagesActive())) {
            if (!addedImages.isEmpty()) {
                detailImages = addedImages.getFirst();
            }
        }

        if (detailImages != null) {
            detailImages.setStatus(1);
            imagesRepository.save(detailImages);
            newProduct.setImages(detailImages);
        }

        productRepository.save(newProduct);
        return true;
    }

    @Override
    public boolean updateProductAPI(UpdateProductRequest request, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        assert email != null;
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        Product existingProduct = productRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        existingProduct.setNameProduct(request.getNameProduct());
        existingProduct.setNameUserUpdate(detailAccount.getFullName());
        existingProduct.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        productRepository.save(existingProduct);
        List<Images> existingImages = imagesRepository.findByProductId(existingProduct.getId());
        for (String fileImages : request.getImagesToAdd()) {
            boolean imageExists = existingImages.stream()
                    .anyMatch(image -> image.getImagesFile().equals(fileImages));

            if (!imageExists) {
                Images newImage = new Images();
                newImage.setCodeImages(gender.generateCode());
                newImage.setImagesFile(fileImages);
                newImage.setSaveToProduct(existingProduct.getId());
                newImage.setNameUserCreate(detailAccount.getFullName());
                newImage.setNameUserUpdate(detailAccount.getFullName());
                newImage.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
                newImage.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
                newImage.setStatus(0);
                imagesRepository.save(newImage);
                existingImages.add(newImage);
            }
        }
        Images activeImage = imagesRepository.findByFileImages(request.getActiveImage());

        if (activeImage == null || !existingImages.contains(activeImage)) {
            activeImage = existingImages.stream()
                    .filter(image -> image.getStatus() == 0)
                    .findFirst()
                    .orElse(null);
        }

        if (activeImage != null) {
            for (Images image : existingImages) {
                image.setStatus(0);
                imagesRepository.save(image);
            }
            activeImage.setStatus(1);
            imagesRepository.save(activeImage);

            existingProduct.setImages(activeImage);
        }

        productRepository.save(existingProduct);

        for (String imageId : request.getImagesToDelete()) {
            Images imageToDelete = imagesRepository.findById(Long.parseLong(imageId))
                    .orElseThrow(() -> new RuntimeException("Image not found"));
            if (existingProduct.getImages() != null && existingProduct.getImages().getId().equals(imageToDelete.getId())) {
                existingProduct.setImages(null);
                productRepository.save(existingProduct);
            }
            imagesRepository.delete(imageToDelete);
        }

        return true;
    }


    @Override
    public String detailProduct(Model model,
                                HttpSession session,
                                Long idProduct) {
        Account detailAccount = gender.checkMenuAdmin(model, session);
        if (detailAccount == null) {
            return "redirect:/mangostore/home";
        } else {
            List<Product> itemsProductInactive = productRepository.getAllProductByStatus0();
            model.addAttribute("listProductInactive", itemsProductInactive);

            List<Images> listImage = imagesRepository.findBySaveToProduct(idProduct);
            model.addAttribute("listImage", listImage);

            Product detailProduct = productRepository.findById(idProduct).orElse(null);
            model.addAttribute("detailProduct", detailProduct);

            return "admin/Product/DetailProduct";
        }
    }

    @Override
    public String deleteProduct(Long idProduct) {
        Product deleteProduct = productRepository.findById(idProduct).orElse(null);
        assert deleteProduct != null;
        deleteProduct.setStatus(0);
        productRepository.save(deleteProduct);
        return "redirect:/mangostore/admin/product";
    }

    @Override
    public void updateProduct(UpdateProductRequest request, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Objects.requireNonNull(email, "Email should not be null");

        Account detailAccount = accountRepository.detailAccountByEmail(email);
        Product existingProduct = productRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        existingProduct.setNameProduct(request.getNameProduct());
        existingProduct.setNameUserUpdate(detailAccount.getFullName());
        existingProduct.setDateUpdate(LocalDateTime.now());

        productRepository.save(existingProduct);

        if (request.getImagesToAdd() != null) {
            List<Images> productImages = imagesRepository.findBySaveToProduct(existingProduct.getId());

            for (String fileImage : request.getImagesToAdd()) {
                boolean imageExists = productImages.stream()
                        .anyMatch(image -> image.getImagesFile().equals(fileImage));

                if (!imageExists) {
                    Images newImage = new Images();
                    newImage.setCodeImages(gender.generateCode());
                    newImage.setImagesFile(fileImage);
                    newImage.setSaveToProduct(existingProduct.getId());
                    newImage.setNameUserCreate(detailAccount.getFullName());
                    newImage.setNameUserUpdate(detailAccount.getFullName());
                    newImage.setDateCreate(LocalDateTime.now());
                    newImage.setDateUpdate(LocalDateTime.now());
                    newImage.setStatus(0);
                    imagesRepository.save(newImage);
                }
            }
        }

        if (request.getImagesToDelete() != null) {
            for (String imageFile : request.getImagesToDelete()) {

                Images imageToDelete = imagesRepository.findByFileImages(imageFile);
                if (imageToDelete != null) {
                    imagesRepository.delete(imageToDelete);
                }
            }
        }

        if (request.getActiveImage() != null) {
            Images detailImage = imagesRepository.findByFileImages(request.getActiveImage());
            if (detailImage != null) {
                detailImage.setStatus(1);
                imagesRepository.save(detailImage);
                existingProduct.setImages(detailImage);
                productRepository.save(existingProduct);
            }
        }
    }

    @Override
    public String restoreProduct(Long aLong) {
        Product product = productRepository.findById(aLong).orElse(null);
        assert product != null;
        product.setStatus(1);
        productRepository.save(product);
        return "redirect:/mangostore/admin/product";
    }

    @Override
    public Integer findByProductCreateExit(String name) {
        Product product = productRepository.findByNameExit(name);
        if (product != null) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public Integer findByProductUpdateExit(String name,
                                           String codeProduct) {
        Product productByName = productRepository.findByNameExit(name);
        if (productByName != null) {
            if (productByName.getCodeProduct().equals(codeProduct)) {
                return 1;
            } else if (productByName.getNameProduct().equals(name)) {
                return 1;
            } else {
                return 2;
            }
        } else {
            return 3;
        }
    }
}