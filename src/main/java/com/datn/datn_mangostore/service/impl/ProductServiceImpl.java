package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.bean.*;
import com.datn.datn_mangostore.config.Gender;
import com.datn.datn_mangostore.repository.AccountRepository;
import com.datn.datn_mangostore.repository.ImagesRepository;
import com.datn.datn_mangostore.repository.ProductRepository;
import com.datn.datn_mangostore.repository.RoleRepository;
import com.datn.datn_mangostore.request.AddProductRequest;
import com.datn.datn_mangostore.request.UpdateProductRequest;
import com.datn.datn_mangostore.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final RoleRepository roleRepository;
    private final ProductRepository productRepository;
    private final Gender gender;
    private final ImagesRepository imagesRepository;
    public ProductServiceImpl(AccountRepository accountRepository,
                              RoleRepository roleRepository,
                              ProductRepository productRepository,
                              Gender gender,
                              ImagesRepository imagesRepository
    ) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.productRepository = productRepository;
        this.gender = gender;
        this.imagesRepository = imagesRepository;

    }





    @Override
    public String indexProductAdmin(Model model,
                                    HttpSession session,
                                    String keyword) {
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
                Role detailRole = roleRepository.getRoleByEmail(email);
                if (detailRole.getName().equals("ADMIN")) {
                    model.addAttribute("checkMenuAdmin", true);
                } else {
                    model.addAttribute("checkMenuAdmin", false);
                }

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
    }

    @Override
    public String createProductAdmin(Model model,
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

                Role detailRole = roleRepository.getRoleByEmail(email);
                if (detailRole.getName().equals("ADMIN")) {
                    model.addAttribute("checkMenuAdmin", true);
                } else {
                    model.addAttribute("checkMenuAdmin", false);
                }

                List<Product> itemsProductInactive = productRepository.getAllProductByStatus0();
                model.addAttribute("listProductInactive", itemsProductInactive);
                return "admin/Product/CreateProduct";
            }
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

        // Fetch existing images
        List<String> existingImages = imagesRepository.findAllImagesFiles();

        // Track added images
        List<Images> addedImages = new ArrayList<>();

        for (String fileImages : request.getListImages()) {
            if (!existingImages.contains(fileImages)) {
                Images newImage = new Images();
                newImage.setCodeImages(gender.generateCode());
                newImage.setImagesFile(fileImages);
                newImage.setSaveToProduct(newProduct.getId());
                newImage.setNameUserCreate(detailAccount.getFullName());
//                newImage.setNameUserUpdate(detailAccount.getFullName());
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
                detailImages = addedImages.get(0);
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

        // Find the existing product by ID or some unique identifier
        Product existingProduct = productRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Update product details
        existingProduct.setNameProduct(request.getNameProduct());
        existingProduct.setNameUserUpdate(detailAccount.getFullName());
        existingProduct.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        productRepository.save(existingProduct);

        // Fetch existing images for this product
        List<Images> existingImages = imagesRepository.findByProductId(existingProduct.getId());

        // Handle new images
        for (String fileImages : request.getImagesToAdd()) {
            boolean imageExists = existingImages.stream()
                    .anyMatch(image -> image.getImagesFile().equals(fileImages));

            if (!imageExists) {
                Images newImage = new Images();
                newImage.setCodeImages(gender.generateCode());
                newImage.setImagesFile(fileImages);
                newImage.setSaveToProduct(existingProduct.getId());
                newImage.setNameUserCreate(detailAccount.getFullName());
//                newImage.setNameUserUpdate(detailAccount.getFullName());
                newImage.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
                newImage.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
                newImage.setStatus(0);
                imagesRepository.save(newImage);
                existingImages.add(newImage);
            }
        }

        // Update active image status
        Images activeImage = imagesRepository.findByFileImages(request.getActiveImage());

        if (activeImage == null || !existingImages.contains(activeImage)) {
            // If the provided active image is not found, use the first image in the list
            activeImage = existingImages.stream()
                    .filter(image -> image.getStatus() == 0)
                    .findFirst()
                    .orElse(null);
        }

        if (activeImage != null) {
            // Reset status for all images
            for (Images image : existingImages) {
                image.setStatus(0);
                imagesRepository.save(image);
            }
            // Set the selected image as active
            activeImage.setStatus(1);
            imagesRepository.save(activeImage);

            // Link active image to the product
            existingProduct.setImages(activeImage);
        }

        productRepository.save(existingProduct);

        // Handle deleting images
        for (String imageId : request.getImagesToDelete()) {
            Images imageToDelete = imagesRepository.findById(Long.parseLong(imageId))
                    .orElseThrow(() -> new RuntimeException("Image not found"));
            // Unlink the image from the product before deleting
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

                Role detailRole = roleRepository.getRoleByEmail(email);
                if (detailRole.getName().equals("ADMIN")) {
                    model.addAttribute("checkMenuAdmin", true);
                } else {
                    model.addAttribute("checkMenuAdmin", false);
                }

                List<Product> itemsProductInactive = productRepository.getAllProductByStatus0();
                model.addAttribute("listProductInactive", itemsProductInactive);

                List<Images> listImage = imagesRepository.findBySaveToProduct(idProduct);
                model.addAttribute("listImage", listImage);

                Product detailProduct = productRepository.findById(idProduct).orElse(null);
                model.addAttribute("detailProduct", detailProduct);


                return "admin/Product/DetailProduct";
            }
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

        // Lấy thông tin tài khoản chi tiết
        Account detailAccount = accountRepository.detailAccountByEmail(email);
// Lấy thông tin sản phẩm hiện tại từ cơ sở dữ liệu
        Product existingProduct = productRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Cập nhật các thông tin sản phẩm
        existingProduct.setNameProduct(request.getNameProduct());
        existingProduct.setNameUserUpdate(detailAccount.getFullName());
        existingProduct.setDateUpdate(LocalDateTime.now());

        // Lưu sản phẩm đã cập nhật
        productRepository.save(existingProduct);

        // Xử lý các thao tác với hình ảnh
        if (request.getImagesToAdd() != null) {
            // Lấy danh sách các ảnh hiện có của sản phẩm
            List<Images> productImages = imagesRepository.findBySaveToProduct(existingProduct.getId());

            for (String fileImage : request.getImagesToAdd()) {
                // Kiểm tra xem ảnh đã tồn tại trong danh sách ảnh của sản phẩm chưa
                boolean imageExists = productImages.stream()
                        .anyMatch(image -> image.getImagesFile().equals(fileImage));

                if (!imageExists) {
                    // Thêm ảnh mới vào danh sách ảnh của sản phẩm
                    Images newImage = new Images();
                    newImage.setCodeImages(gender.generateCode());
                    newImage.setImagesFile(fileImage);
                    newImage.setSaveToProduct(existingProduct.getId());
                    newImage.setNameUserCreate(detailAccount.getFullName());
                    newImage.setNameUserUpdate(detailAccount.getFullName());
                    newImage.setDateCreate(LocalDateTime.now());
                    newImage.setDateUpdate(LocalDateTime.now());
                    newImage.setStatus(0); // Thiết lập trạng thái mặc định cho ảnh mới
                    imagesRepository.save(newImage);
                }
            }
        }

        if (request.getImagesToDelete() != null) {
            for (String imageFile : request.getImagesToDelete()) {
                // Xóa ảnh khỏi cơ sở dữ liệu
                Images imageToDelete = imagesRepository.findByFileImages(imageFile);
                if (imageToDelete != null) {
                    imagesRepository.delete(imageToDelete);
                    // Tùy chọn: Xóa tập tin vật lý từ bộ nhớ lưu trữ
                    // Ví dụ: FileUtils.delete(imageFile);
                }
            }
        }

        // Cập nhật ảnh đang hoạt động
        if (request.getActiveImage() != null) {
            // Tìm ảnh dựa trên tên tệp ảnh
            Images detailImage = imagesRepository.findByFileImages(request.getActiveImage());
            if (detailImage != null) {
                // Đặt trạng thái của ảnh thành hoạt động (1)
                detailImage.setStatus(1);
                imagesRepository.save(detailImage);

                // Liên kết ảnh hoạt động với sản phẩm
                existingProduct.setImages(detailImage);
                productRepository.save(existingProduct);
            }
        }
    }

    @Override
    public String restoreProduct(Long aLong) {
        Product product = productRepository.findById(aLong).orElse(null);
        product.setStatus(1);
        productRepository.save(product);
        return "redirect:/mangostore/admin/product";
    }


    @Override
    public Integer findByProductCreateExit(String name) {
        Product product = productRepository.findByNameExit(name);
        if (product != null) {
            return 1; // Tồn tại
        } else {
            return 2; // Chưa có
        }
    }

    @Override
    public Integer findByProductUpdateExit(String name, String codeProduct) {
        Product productByName = productRepository.findByNameExit(name);
        if (productByName != null) {
            // Nếu tìm thấy sản phẩm có cùng name
            if (productByName.getCodeProduct().equals(codeProduct)) {
                return 1; // Sản phẩm tồn tại và codeProduct trùng khớp (Update)
            } else {
                return 2; // Chỉ có name tồn tại, nhưng codeProduct khác (Name đã tồn tại)
            }
        } else {
            return 3; // Name chưa tồn tại (Create mới)
        }
    }


}