package com.imageservice.service.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.imageservice.exception.business.image.StorageException;
import com.imageservice.service.FileService;
import com.imageservice.service.StorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocalStorageService implements StorageService {
    private final FileService fileService;

    @Override
    public String store(MultipartFile file, String filePath) throws IOException {
        try {
            // 存到本地
            Path targetPath = Paths.get(fileService.getActualUploadPath()).resolve(filePath); // final destination
            Files.createDirectories(targetPath.getParent()); // 创建文件夹
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return filePath;
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filePath, e);
        }
    }

}
