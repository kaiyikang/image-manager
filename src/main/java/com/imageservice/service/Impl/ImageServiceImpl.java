package com.imageservice.service.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.imageservice.entity.Image;
import com.imageservice.exception.business.image.InvalidImageException;
import com.imageservice.exception.business.image.StorageException;
import com.imageservice.repository.ImageRepository;
import com.imageservice.service.FileService;
import com.imageservice.service.ImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final FileService fileService;

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

            // 存到本地
            Path targetPath = Paths.get(fileService.getActualUploadPath()).resolve(filePath); // final destination
            Files.createDirectories(targetPath.getParent()); // 创建
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // 保存到db
            Image image = Image.builder()
                    .name(filename)
                    .filePath(filePath)
                    .contentType(contentType)
                    .fileSize(file.getSize())
                    .createdAt(LocalDateTime.now())
                    .build();

            return imageRepository.save(image);
        } catch (IOException e) {
            throw new StorageException("Failed to store file", e);
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
