package com.jansetu4.portal.citizen.service;

import com.jansetu4.portal.common.exceptions.BadRequestException;
import com.jansetu4.portal.config.FileStorageConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalFileStorageService implements FileStorageService {

    private final FileStorageConfig fileStorageConfig;

    @PostConstruct
    public void initialize() {
        try {
            Files.createDirectories(getRootPath());
        } catch (IOException ex) {
            throw new BadRequestException("Unable to initialize file storage");
        }
    }

    @Override
    public String store(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Uploaded file cannot be empty");
        }

        String originalName = StringUtils.cleanPath(Objects.requireNonNullElse(file.getOriginalFilename(), "file"));
        String filename = UUID.randomUUID() + "-" + originalName.replace(" ", "_");
        Path targetDir = getRootPath().resolve(subDir).normalize();

        try {
            Files.createDirectories(targetDir);
            Path targetFile = targetDir.resolve(filename);
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + subDir + "/" + filename;
        } catch (IOException ex) {
            throw new BadRequestException("Unable to store file: " + originalName);
        }
    }

    @Override
    public Resource load(String url) {
        String relativePath = url.startsWith("/uploads/") ? url.substring("/uploads/".length()) : url;
        Path filePath = getRootPath().resolve(relativePath).normalize();
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new BadRequestException("File not found");
            }
            return resource;
        } catch (MalformedURLException ex) {
            throw new BadRequestException("Invalid file path");
        }
    }

    private Path getRootPath() {
        return Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();
    }
}
