package org.enigma.tokonyadia_api.repository;

import org.enigma.tokonyadia_api.entity.ProductPromo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPromoRepository extends JpaRepository<ProductPromo, String>, JpaSpecificationExecutor<ProductPromo> {
}
