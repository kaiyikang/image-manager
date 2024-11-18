package com.imageservice.service;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
public class FileService {
    @Value("${file.upload.path}")
    private String uploadPath;

    public String getActualUploadPath() {
        try {
            String prefix = "classpath:";
            if (uploadPath.startsWith(prefix)) {
                return ResourceUtils.getURL(prefix).getPath() + uploadPath.substring(prefix.length());
            }
            return uploadPath;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot resolve the upload path", e);
        }
    }

}
