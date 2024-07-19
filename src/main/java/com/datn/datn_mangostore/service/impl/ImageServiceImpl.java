package com.datn.datn_mangostore.service.impl;

import com.datn.datn_mangostore.repository.ImagesRepository;
import com.datn.datn_mangostore.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImagesRepository imagesRepository;


    @Override
    public String deleteImage(Long idOrigin) {
        return null;
    }


//    vinh
    @Override
    public List<String> findAllImagesFiles() {
        return imagesRepository.findAllImagesFiles();
    }


}
