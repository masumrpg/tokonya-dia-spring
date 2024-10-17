package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.ProductPromoRequest;
import org.enigma.tokonyadia_api.dto.request.SearchWithMinMaxRequest;
import org.enigma.tokonyadia_api.dto.response.ProductPromoResponse;
import org.enigma.tokonyadia_api.entity.ProductPromo;
import org.springframework.data.domain.Page;

public interface ProductPromoService {
    ProductPromoResponse create(ProductPromoRequest request);

    ProductPromoResponse getById(String promoId);

    ProductPromo getOneById(String promoId);

    ProductPromoResponse update(String promoId, ProductPromoRequest request);

    void delete(String promoId);

    Page<ProductPromoResponse> getAll(SearchWithMinMaxRequest request);
}
