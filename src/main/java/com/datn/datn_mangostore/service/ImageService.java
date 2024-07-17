package com.datn.datn_mangostore.service;

import java.util.List;

public interface ImageService {

    String deleteImage(Long idOrigin);

    List<String> findAllImagesFiles();


}
