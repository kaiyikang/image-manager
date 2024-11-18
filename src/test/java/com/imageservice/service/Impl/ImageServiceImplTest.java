package com.imageservice.service.Impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.imageservice.entity.Image;
import com.imageservice.repository.ImageRepository;

@ExtendWith(MockitoExtension.class)
public class ImageServiceImplTest {
    @Mock
    private ImageRepository imageRepository;

    @Mock
    private LocalStorageService localStorageService;

    @InjectMocks
    private ImageServiceImpl imageService;

    private byte[] imageBytes = "Test image content".getBytes();

    @BeforeEach
    void setUp() {

    }

    @Test
    void uploadImage_Success() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.jpg",
                "image/jpeg",
                imageBytes);
        String expectedFilePath = "test-image.jpg";
        Image expectedImage = Image.builder()
                .id(1)
                .name("test-image.jpg")
                .filePath(expectedFilePath)
                .fileSize(Long.valueOf(imageBytes.length))
                .createdAt(LocalDateTime.now())
                .build();
        when(localStorageService.store(any(MultipartFile.class), anyString())).thenReturn(expectedFilePath);
        when(imageRepository.save(any(Image.class))).thenReturn(expectedImage);

        Image result = imageService.uploadImage(file);
        assertNotNull(result);
        assertEquals(expectedImage.getName(), result.getName());
        assertEquals(expectedImage.getFilePath(), result.getFilePath());
        assertEquals(expectedImage.getFileSize(), result.getFileSize());
        verify(localStorageService, times(1)).store(any(), anyString());
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    void getImageInfo_Success() {
        // Given
        Integer imageId = 1;
        Image expectedImage = Image.builder()
                .id(imageId)
                .name("test.jpg")
                .filePath("2024/test.jpg")
                .contentType("image/jpeg")
                .fileSize(1024L)
                .createdAt(LocalDateTime.now())
                .build();
        when(imageRepository.findById(imageId)).thenReturn(Optional.of(expectedImage));

        // When
        Optional<Image> result = imageService.getImageInfo(imageId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedImage, result.get());
        verify(imageRepository).findById(imageId);

    }

    @Test
    void getImageInfo_NotFound() {
        Integer imageId = 999;
        when(imageRepository.findById(imageId)).thenReturn(Optional.empty());

        // When
        Optional<Image> result = imageService.getImageInfo(imageId);

        assertTrue(result.isEmpty());
        verify(imageRepository).findById(imageId);
    }

    @Test
    void getImageInfo_NullId() {
        assertThrows(IllegalArgumentException.class, () -> imageService.getImageInfo(null));
        verify(imageRepository, never()).findById(any());
    }
}
