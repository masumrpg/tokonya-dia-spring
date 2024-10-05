package org.enigma.tokonyadia_api.service.impl;

import org.enigma.tokonyadia_api.dto.request.ProductRequest;
import org.enigma.tokonyadia_api.dto.request.SearchWithGteLtaRequest;
import org.enigma.tokonyadia_api.dto.response.ProductResponse;
import org.enigma.tokonyadia_api.dto.response.StoreResponse;
import org.enigma.tokonyadia_api.entity.Product;
import org.enigma.tokonyadia_api.entity.Store;
import org.enigma.tokonyadia_api.repository.ProductRepository;
import org.enigma.tokonyadia_api.service.ProductService;
import org.enigma.tokonyadia_api.service.StoreService;
import org.enigma.tokonyadia_api.specification.FilterSpecificationBuilder;
import org.enigma.tokonyadia_api.utils.SortUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final StoreService storeService;

    public ProductServiceImpl(ProductRepository productRepository, StoreService storeService) {
        this.productRepository = productRepository;
        this.storeService = storeService;
    }

    @Override
    public ProductResponse create(ProductRequest productRequest) {
        StoreResponse storeResponse = storeService.getById(productRequest.getStoreId());
        Store store = Store.builder()
                .id(storeResponse.getId())
                .siup(storeResponse.getSiup())
                .name(storeResponse.getName())
                .address(storeResponse.getAddress())
                .build();
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .store(store)
                .build();
        productRepository.saveAndFlush(product);

        return toProductResponse(product);
    }

    @Override
    public ProductResponse getById(String id) {
        return toProductResponse(getOne(id));
    }

    @Override
    public ProductResponse update(String id, ProductRequest productRequest) {
        StoreResponse storeResponse = storeService.getById(productRequest.getStoreId());
        Store store = Store.builder()
                .id(storeResponse.getId())
                .siup(storeResponse.getSiup())
                .name(storeResponse.getName())
                .address(storeResponse.getAddress())
                .build();

        Product existingProduct = getOne(id);
        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setStock(productRequest.getStock());
        existingProduct.setStore(store);
        return toProductResponse(existingProduct);
    }

    @Override
    public void delete(String id) {
        Product product = getOne(id);
        productRepository.delete(product);
    }

    @Override
    public Page<ProductResponse> getAll(SearchWithGteLtaRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Product> specification = new FilterSpecificationBuilder<Product>()
                .withLike("name", request.getQuery())
                .withGreaterThanOrEqualTo("price", request.getMinValue())
                .withLessThanOrEqualTo("price", request.getMaxValue())
                .build();
        Page<Product> resultPage = productRepository.findAll(specification, pageable);

        return resultPage.map(product -> toProductResponse(product));
    }

    private Product getOne(String id) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
    }

    private ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .store(product.getStore())
                .build();
    }
}
