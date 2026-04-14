package com.project.ecommercebackend.service.impl;

import com.project.ecommercebackend.exception.FileStorageException;
import com.project.ecommercebackend.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new FileStorageException("File must have a valid name");
        }

        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String fileName = UUID.randomUUID() + extension;
        String filePath = path + File.separator + fileName;

        try {
            File folder = new File(path);
            if (!folder.exists() && !folder.mkdirs()) {
                throw new FileStorageException("Failed to create upload directory: " + path);
            }
            Files.copy(file.getInputStream(), Paths.get(filePath));
        } catch (IOException e) {
            throw new FileStorageException("Failed to store file: " + fileName, e);
        }

        return fileName;
    }
}
