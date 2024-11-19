package com.imageservice.entity;

import org.springframework.core.io.Resource;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageDTO {
    private Resource resource;
    private String contentType;
    private long contentLength;
    private String fileName;
}
