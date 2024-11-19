package com.imageservice.controller;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.imageservice.entity.Image;
import com.imageservice.entity.ImageDTO;
import com.imageservice.exception.business.image.InvalidImageException;
import com.imageservice.exception.business.image.StorageException;
import com.imageservice.service.ImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<Page<Image>> listImages(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            Page<Image> images = imageService.findAll(pageable);
            return ResponseEntity.ok(images);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable Integer id) {
        ImageDTO imageDTO = imageService.getImage(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageDTO.getContentType()))
                .contentLength(imageDTO.getContentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageDTO.getFileName() + "\"")
                .body(imageDTO.getResource());
    }

    @GetMapping("/{id}/info")
    public ResponseEntity<Image> getImageInfo(@PathVariable Integer id) {
        return imageService.getImageInfo(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Image> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty())
                return ResponseEntity.badRequest().build();

            Image savedImage = imageService.uploadImage(file);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedImage);

        } catch (InvalidImageException e) {
            log.warn("Invalid image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (StorageException e) {
            log.error("Storage error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.error("Unexpected error while uploading image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
