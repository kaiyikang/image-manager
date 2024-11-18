package com.imageservice.service.Impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.imageservice.exception.business.image.StorageException;
import com.imageservice.service.FileService;

@ExtendWith(MockitoExtension.class)
public class LocalStorageServiceTest {

    @Mock
    private FileService fileService;

    @InjectMocks
    private LocalStorageService localStorageService;

    @TempDir
    Path tempPath;

    @Test
    void store_ShouldStoredFileSuccessfully() throws IOException {
        // Given
        String fileName = "test-image.jpg";
        String content = "Cat";
        String filePath = "static/uploads/" + fileName;

        MultipartFile file = new MockMultipartFile(
                fileName,
                fileName,
                "image/jpeg",
                content.getBytes());

        when(fileService.getActualUploadPath()).thenReturn(tempPath.toString());

        // When
        String result = localStorageService.store(file, filePath);

        // Then
        assertEquals(filePath, result);
        Path storedPath = tempPath.resolve(filePath);
        assertTrue(Files.exists(storedPath));
        assertEquals(content, Files.readString(storedPath));
    }

    @Test
    void store_ShouldThrowsStorageException_WhenIOExceptionOccurs() throws IOException {
        // Give
        String fileName = "test-image.jpg";
        String filePath = "invalid/path/" + fileName;

        MultipartFile file = new MockMultipartFile(fileName, fileName, "image/jpeg", "content".getBytes());

        when(fileService.getActualUploadPath()).thenReturn("/invalid/path");

        // When
        StorageException exception = assertThrows(StorageException.class,
                () -> localStorageService.store(file, filePath));
        // Then
        assertTrue(exception.getMessage().contains("Failed to store file"));
    }

    @Test
    void store_ShouldCreateDirectories_WhenTheyDontExist() throws IOException {
        // Given
        String fileName = "test-image.jpg";
        String filePath = "new/nested/directories/" + fileName;
        String content = "Cat";

        MultipartFile file = new MockMultipartFile(fileName, fileName, "image/jpeg", content.getBytes());

        when(fileService.getActualUploadPath()).thenReturn(tempPath.toString());

        // When
        String result = localStorageService.store(file, filePath);

        // Then
        assertEquals(filePath, result);
        Path storedFilePath = tempPath.resolve(filePath);
        assertTrue(Files.exists(storedFilePath));
        assertTrue(Files.exists(storedFilePath.getParent()));
        assertEquals(content, Files.readString(storedFilePath));
    }
}
