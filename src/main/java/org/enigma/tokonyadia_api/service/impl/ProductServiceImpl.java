package org.enigma.tokonyadia_api.service.impl;

import org.enigma.tokonyadia_api.dto.request.ProductUpdateRequest;
import org.enigma.tokonyadia_api.entity.Product;
import org.enigma.tokonyadia_api.entity.Store;
import org.enigma.tokonyadia_api.repository.ProductRepository;
import org.enigma.tokonyadia_api.repository.StoreRepository;
import org.enigma.tokonyadia_api.service.ProductService;
import org.enigma.tokonyadia_api.service.StoreService;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product getById(String id) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        throw new RuntimeException("Produk tidak ada!");
    }

    @Override
    public Product update(String id, ProductUpdateRequest product) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isPresent()) {
            Product updatedProduct = byId.get();
            updatedProduct.setName(product.getName());
            updatedProduct.setDescription(product.getDescription());
            updatedProduct.setPrice(product.getPrice());
            updatedProduct.setStock(product.getStock());
            // FIXED
            Store store = storeService.getById(product.getStoreId());
            System.out.println(store);
            if (store != null) {
                updatedProduct.setStore(store);
            }
            return productRepository.save(updatedProduct);
        }
        return null;
    }

    @Override
    public String delete(String id) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isPresent()) {
            productRepository.delete(byId.get());
            return "Produk dengan id " + id + " telah dihapus!";
        }
        return "Produk tidak ada!";
    }

    @Override
    public List<Product> getAll() {
        List<Product> productList = productRepository.findAll();
        if (productList.isEmpty()) {
            throw new RuntimeException("Produk tidak ada!");
        }
        return productList;
    }
}
