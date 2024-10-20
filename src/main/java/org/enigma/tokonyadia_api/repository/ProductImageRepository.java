package org.enigma.tokonyadia_api.repository;

import org.enigma.tokonyadia_api.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductImageRepository extends JpaRepository<ProductImage, String>, JpaSpecificationExecutor<ProductImage> {
}
