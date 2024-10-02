package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.entity.Product;

import java.util.List;

public interface ProductService {
    Product create(Product product);
    Product getById(String id);
    Product update(String id, Product product);
    String delete(String id);
    List<Product> getAll();
}
