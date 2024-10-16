package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.response.ShipmentResponse;
import org.enigma.tokonyadia_api.entity.Shipment;
import org.springframework.data.domain.Page;

public interface ShipmentService {
    ShipmentResponse getById(String shipmentId);

    Shipment getOne(String shipmentId);

    Page<ShipmentResponse> getAll(SearchCommonRequest request);
}
