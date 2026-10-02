package com.personal.image.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.personal.image.entity.Image;
import com.personal.image.entity.User;
import com.personal.image.repository.ImageRepository;
import com.personal.image.repository.UserRepository;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    public ImageService(ImageRepository imageRepository,
                        UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    public Image uploadImage(String username,
                             String title,
                             String description,
                             MultipartFile file) throws IOException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (file.isEmpty()) {
            throw new RuntimeException("Image file is empty");
        }

        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Only image files are allowed");
        }

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();

        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(
                    originalFilename.lastIndexOf(".")
            );
        }

        String storedFilename = UUID.randomUUID() + extension;

        Path filePath = uploadPath.resolve(storedFilename);

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        Image image = new Image();

        image.setTitle(title);
        image.setDescription(description);
        image.setFilename(originalFilename);
        image.setFilePath(filePath.toString());
        image.setContentType(contentType);
        image.setFileSize(file.getSize());
        image.setUploadedAt(LocalDateTime.now());
        image.setUser(user);

        return imageRepository.save(image);
    }

    public List<Image> getMyImages(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return imageRepository.findByUser(user);
    }
}