package org.enigma.tokonyadia_api.repository;

import org.enigma.tokonyadia_api.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String>, JpaSpecificationExecutor<ProductCategory> {
}
