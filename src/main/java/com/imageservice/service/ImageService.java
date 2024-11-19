package com.imageservice.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.imageservice.entity.Image;
import com.imageservice.entity.ImageDTO;
import com.imageservice.exception.business.image.InvalidImageException;
import com.imageservice.exception.business.image.StorageException;

public interface ImageService {
    Image uploadImage(MultipartFile file) throws InvalidImageException, StorageException;

    ImageDTO getImage(Integer id);

    Optional<Image> getImageInfo(Integer id);

    Page<Image> findAll(Pageable pageable);
}
