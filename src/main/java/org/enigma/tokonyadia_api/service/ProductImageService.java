package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.response.FileDownloadResponse;
import org.enigma.tokonyadia_api.entity.Product;
import org.enigma.tokonyadia_api.entity.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {
    ProductImage create(MultipartFile multipartFile, Product product);

    List<ProductImage> createBulk(List<MultipartFile> multipartFiles, Product product);

    ProductImage update(String productId, MultipartFile multipartFile);

    void deleteById(String id);

    FileDownloadResponse downloadImage(String id);
}
