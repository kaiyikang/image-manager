package com.imageservice.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.imageservice.entity.Image;
import com.imageservice.repository.ImageRepository;
import com.imageservice.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final ImageRepository imageRepository;
    private final FileService fileService;

    // @GetMapping
    // public ResponseEntity<Page<Image>> listImages() {

    // }

    // @GetMapping("/{id}")
    // public ResponseEntity<Resource> getImage(@PathVariable Integer id) {

    // }

    @GetMapping("/{id}/info")
    public ResponseEntity<Image> getImageInfo(@PathVariable Integer id) {
        return imageRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Image> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // 是否有输入
            if (file.isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            // 输入类型对么
            String contentType = file.getContentType();
            if (!isValidContentType(contentType))
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();

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
                    .contentType(contentType)
                    .fileSize(file.getSize())
                    .createdAt(LocalDateTime.now())
                    .build();

            Image savedImage = imageRepository.save(image);

            // 成功响应
            return ResponseEntity.ok(savedImage);

        } catch (IllegalArgumentException e) {
            log.warn("Valication failed: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            log.error("Fialed to store file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.error("Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
