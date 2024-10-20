package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.dto.request.ProductRequest;
import org.enigma.tokonyadia_api.dto.request.SearchWithMinMaxRequest;
import org.enigma.tokonyadia_api.dto.request.UpdateProductRequest;
import org.enigma.tokonyadia_api.dto.response.ProductResponse;
import org.enigma.tokonyadia_api.entity.Product;
import org.enigma.tokonyadia_api.entity.ProductCategory;
import org.enigma.tokonyadia_api.entity.ProductImage;
import org.enigma.tokonyadia_api.entity.Store;
import org.enigma.tokonyadia_api.repository.ProductRepository;
import org.enigma.tokonyadia_api.service.ProductCategoryService;
import org.enigma.tokonyadia_api.service.ProductImageService;
import org.enigma.tokonyadia_api.service.ProductService;
import org.enigma.tokonyadia_api.service.StoreService;
import org.enigma.tokonyadia_api.specification.FilterSpecificationBuilder;
import org.enigma.tokonyadia_api.util.MapperUtil;
import org.enigma.tokonyadia_api.util.SortUtil;
import org.enigma.tokonyadia_api.util.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.enigma.tokonyadia_api.util.MapperUtil.toProductResponse;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;
    private final ProductImageService productImageService;
    private final StoreService storeService;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductResponse create(List<MultipartFile> multipartFiles, ProductRequest request) {
        validationUtil.validate(request);
        Store store = storeService.getOne(request.getStoreId());
        ProductCategory productCategory = productCategoryService.getOne(request.getCategoryId());
        Product product = Product.builder()
                .name(request.getName())
                .category(productCategory)
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .store(store)
                .build();
        productRepository.saveAndFlush(product);
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            List<ProductImage> menuImages = productImageService.createBulk(multipartFiles, product);
            product.setProductImages(menuImages);
        }
        return toProductResponse(product);
    }

    @Transactional(readOnly = true)
    @Override
    public Product getOne(String id) {
        return productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse getById(String id) {
        return toProductResponse(getOne(id));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductResponse update(String id, UpdateProductRequest request) {
        validationUtil.validate(request);
        Store store = storeService.getOne(request.getStoreId());
        ProductCategory productCategory = productCategoryService.getOne(request.getCategoryId());

        Product existingProduct = getOne(id);
        existingProduct.setName(request.getName());
        existingProduct.setCategory(productCategory);
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStock(request.getStock());
        existingProduct.setStore(store);
        return toProductResponse(existingProduct);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        productRepository.delete(getOne(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponse> getAll(SearchWithMinMaxRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Product> specification = new FilterSpecificationBuilder<Product>()
                .withLike("name", request.getQuery())
                .withGreaterThanOrEqualTo("price", request.getMinValue())
                .withLessThanOrEqualTo("price", request.getMaxValue())
                .build();
        Page<Product> resultPage = productRepository.findAll(specification, pageable);

        return resultPage.map(MapperUtil::toProductResponse);
    }
}
