package com.imageservice.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(MultipartFile file, String filePath) throws IOException;

    // void delete(String filePath) throws IOException;
}
