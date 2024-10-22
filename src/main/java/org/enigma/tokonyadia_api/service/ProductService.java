package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.ProductRequest;
import org.enigma.tokonyadia_api.dto.request.SearchWithMinMaxRequest;
import org.enigma.tokonyadia_api.dto.request.UpdateProductRequest;
import org.enigma.tokonyadia_api.dto.response.ProductResponse;
import org.enigma.tokonyadia_api.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductResponse create(List<MultipartFile> multipartFiles, ProductRequest request);

    Product getOne(String id);

    ProductResponse getById(String id);

    ProductResponse update(String id, List<MultipartFile> files, UpdateProductRequest product);

    void update(Product product);

    void deleteById(String id);

    ProductResponse updateImage(MultipartFile file, String productId);

    void deleteImage(String imageId);

    Page<ProductResponse> getAll(SearchWithMinMaxRequest request);
}
