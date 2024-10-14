package com.scm.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uploadImage(MultipartFile contactPic, String fileName);
    String getUrlFromPublicId(String publicId);
}
