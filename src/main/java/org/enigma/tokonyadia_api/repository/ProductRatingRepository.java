package org.enigma.tokonyadia_api.repository;

import org.enigma.tokonyadia_api.entity.ProductRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRatingRepository extends JpaRepository<ProductRating, String>, JpaSpecificationExecutor<ProductRating> {
}
