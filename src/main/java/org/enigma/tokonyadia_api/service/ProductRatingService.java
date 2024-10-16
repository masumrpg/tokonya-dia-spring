package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.ProductRatingRequest;
import org.enigma.tokonyadia_api.dto.request.SearchWithMinMaxRequest;
import org.enigma.tokonyadia_api.dto.request.UpdateProductRatingRequest;
import org.enigma.tokonyadia_api.dto.response.ProductRatingResponse;
import org.enigma.tokonyadia_api.entity.ProductRating;
import org.springframework.data.domain.Page;

public interface ProductRatingService {
    ProductRatingResponse create(ProductRatingRequest request);

    ProductRatingResponse getById(String id);

    ProductRating getOneById(String id);

    ProductRatingResponse update(String id, UpdateProductRatingRequest request);

    void delete(String id);

    Page<ProductRatingResponse> getAll(SearchWithMinMaxRequest request);
}
