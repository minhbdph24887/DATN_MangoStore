package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.repository.ImagesRepository;
import com.datn.datn_mangostore.service.ImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImagesRepository imagesRepository;

    public ImageServiceImpl(ImagesRepository imagesRepository) {
        this.imagesRepository = imagesRepository;
    }

    @Override
    public List<String> findAllImagesFiles() {
        return imagesRepository.findAllImagesFiles();
    }
}
