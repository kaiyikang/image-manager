package com.imageservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.imageservice.domain.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByContentType(String contentType);

    Optional<Image> findByFilePath(String filePath);

    @Query("SELECT i FROM Image i WHERE i.fileSize > :minSize")
    List<Image> findLargeImage(@Param("minSize") Long minSize);

    @Query("SELECT i FROM Image i ORDER BY i.createdAt DESC LIMIT :limit")
    Optional<Image> findRecentImages(@Param("limit") int limit);

}
