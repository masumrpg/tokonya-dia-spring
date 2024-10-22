package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.enigma.tokonyadia_api.constant.FileType;
import org.enigma.tokonyadia_api.dto.response.FileDownloadResponse;
import org.enigma.tokonyadia_api.dto.response.FileInfo;
import org.enigma.tokonyadia_api.entity.Product;
import org.enigma.tokonyadia_api.entity.ProductImage;
import org.enigma.tokonyadia_api.repository.ProductImageRepository;
import org.enigma.tokonyadia_api.service.FileStorageService;
import org.enigma.tokonyadia_api.service.ProductImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final FileStorageService fileStorageService;
    private final List<String> contentTypes = List.of("image/jpg", "image/png", "image/webp", "image/jpeg");
    private final String PRODUCT = "product";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductImage create(MultipartFile multipartFile, Product product) {
        FileInfo fileInfo = fileStorageService.storeFile(FileType.IMAGE, PRODUCT, multipartFile, contentTypes);
        ProductImage newProductImage = ProductImage.builder()
                .fileName(fileInfo.getFilename())
                .product(product)
                .contentType(multipartFile.getContentType())
                .size(multipartFile.getSize())
                .path(fileInfo.getPath())
                .build();
        return productImageRepository.saveAndFlush(newProductImage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ProductImage> createBulk(List<MultipartFile> multipartFiles, Product product) {
        return multipartFiles.stream().map(multipartFile -> create(multipartFile, product)).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductImage update(String productId, MultipartFile multipartFile) {
        ProductImage productImage = findByIdOrThrowNotFound(productId);
        FileInfo fileInfo = fileStorageService.storeFile(FileType.IMAGE, PRODUCT, multipartFile, contentTypes);
        fileStorageService.deleteFile(productImage.getPath());
        productImage.setPath(fileInfo.getPath());
        productImageRepository.saveAndFlush(productImage);
        return productImage;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        ProductImage productImage = findByIdOrThrowNotFound(id);
        String path = productImage.getPath();
        productImageRepository.delete(productImage);
        fileStorageService.deleteFile(path);
    }

    @Transactional(readOnly = true)
    @Override
    public FileDownloadResponse downloadImage(String id) {
        ProductImage productImage = productImageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "image not found"));
        Resource resource = fileStorageService.readFile(productImage.getPath());
        return FileDownloadResponse.builder()
                .resource(resource)
                .contentType(productImage.getContentType())
                .build();
    }

    @Transactional(readOnly = true)
    public ProductImage findByIdOrThrowNotFound(String id) {
        return productImageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "image not found"));
    }
}
