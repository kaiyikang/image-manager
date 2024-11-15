package com.imageservice.controller;

import org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.imageservice.repository.ImageRepository;
import com.imageservice.domain.entity.Image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final ImageRepository imageRepository;

    @GetMapping
    public ResponseEntity<Page<Image>> listImages() {

    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable Integer id) {

    }

    @GetMapping("/{id}/info")
    public ResponseEntity<Image> getImageInfo(@PathVariable Integer id) {
        return imageRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Image> uploadImage(@RequestParam("file") MultipartFile file) {

    }
}
