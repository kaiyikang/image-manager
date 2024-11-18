package com.imageservice.service;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.imageservice.entity.Image;
import com.imageservice.exception.business.image.InvalidImageException;
import com.imageservice.exception.business.image.StorageException;

public interface ImageService {
    Image uploadImage(MultipartFile file) throws InvalidImageException, StorageException;

    Optional<Image> getImageInfo(Integer id);
}
