package com.imageservice.service.Impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.imageservice.entity.Image;
import com.imageservice.exception.business.image.ImageServiceException;
import com.imageservice.exception.business.image.InvalidImageException;
import com.imageservice.exception.business.image.StorageException;
import com.imageservice.repository.ImageRepository;
import com.imageservice.service.ImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final LocalStorageService localStorageService;

    @Override
    @Transactional
    public Image uploadImage(MultipartFile file) throws InvalidImageException, StorageException {
        // File type check
        String contentType = file.getContentType();
        if (!isValidContentType(contentType)) {
            throw new InvalidImageException("Unsupported image type: " + contentType);
        }
        try {
            // 存到哪里？
            String filename = file.getOriginalFilename();
            String filePath = generateFilePath(filename);

            // 存储到本地
            String savedPath = localStorageService.store(file, filePath);

            // 保存到db
            Image image = Image.builder()
                    .name(filename)
                    .filePath(savedPath)
                    .contentType(contentType)
                    .fileSize(file.getSize())
                    .createdAt(LocalDateTime.now())
                    .build();

            return imageRepository.save(image);

        } catch (IOException e) {
            throw new StorageException("Failed to store file", e);
        }
    }

    @Override
    public Optional<Image> getImageInfo(Integer id) {
        if (id == null)
            throw new IllegalArgumentException("Image id cannot be null");

        return imageRepository.findById(id);
    }

    @Override
    public Page<Image> findAll(Pageable pageable) {
        log.debug("Fetching images page {} with size {}",
                pageable.getPageNumber(),
                pageable.getPageSize());
        if (pageable.getPageSize() > 100) {
            throw new IllegalArgumentException("Page size must not be greater than 100");
        }
        try {
            Page<Image> images = imageRepository.findAll(pageable);
            return images;
        } catch (Exception e) {
            log.error("Error fetching images", e);
            throw new ImageServiceException("Failed to fetch images", e);
        }
    }

    private boolean isValidContentType(String contentType) {
        if (contentType == null)
            return false;
        return contentType.startsWith("image/");
    }

    private String generateFilePath(String filename) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomStr = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(filename);
        return String.format("%s_%s%s", timestamp, randomStr, extension);
    }

    private String getFileExtension(String filename) {
        if (filename == null)
            return "";
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex);
    }

}
