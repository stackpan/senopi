package com.ivanzkyanto.senopi.service.impl;

import com.ivanzkyanto.senopi.configuration.StorageProperties;
import com.ivanzkyanto.senopi.exception.StorageException;
import com.ivanzkyanto.senopi.exception.StorageFileNotFoundException;
import com.ivanzkyanto.senopi.service.StorageService;
import com.ivanzkyanto.senopi.service.ValidationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    private final ValidationService validationService;

    public FileSystemStorageService(StorageProperties storageProperties, ValidationService validationService) {
        if (storageProperties.getLocation().trim().isEmpty()) {
            throw new StorageException("File upload location can not be Empty.");
        }

        rootLocation = Paths.get(storageProperties.getLocation());
        this.validationService = validationService;
    }

    @Override
    public String store(MultipartFile file) {
        validationService.validateMultipartContentType(file, "image/png", "image/jpeg", "image/webp");

        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }

            Path destinationFile = rootLocation.resolve(
                            Paths.get(Objects.requireNonNull(file.getOriginalFilename())))
                    .normalize();
            Path absoluteDestinationFile = destinationFile.toAbsolutePath();

            if (!absoluteDestinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException("Cannot store file outside current directory.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, absoluteDestinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return destinationFile.getFileName().toString();
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public List<Path> loadAll() {
        try (Stream<Path> stream = Files.walk(rootLocation, 1)
                .filter(path -> !path.equals(rootLocation))
                .map(rootLocation::relativize)) {
            return stream.collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists() && !resource.isReadable()) {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
