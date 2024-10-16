package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.response.ShipmentResponse;
import org.enigma.tokonyadia_api.entity.Shipment;
import org.enigma.tokonyadia_api.repository.ShipmentRepository;
import org.enigma.tokonyadia_api.service.ShipmentService;
import org.enigma.tokonyadia_api.specification.FilterSpecificationBuilder;
import org.enigma.tokonyadia_api.util.MapperUtil;
import org.enigma.tokonyadia_api.util.SortUtil;
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
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentRepository shipmentRepository;

    @Override
    public ShipmentResponse getById(String shipmentId) {
        Shipment shipment = getOne(shipmentId);
        return MapperUtil.toShipmentResponse(shipment);
    }

    @Override
    public Shipment getOne(String shipmentId) {
        Optional<Shipment> optionalShipment = shipmentRepository.findById(shipmentId);
        return optionalShipment.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_SHIPMENT_NOT_FOUND));
    }

    @Override
    public Page<ShipmentResponse> getAll(SearchCommonRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Shipment> specification = new FilterSpecificationBuilder<Shipment>()
                .withLike("courierName", request.getQuery())
                .withEqual("status", request.getQuery())
                .build();
        Page<Shipment> resultPage = shipmentRepository.findAll(specification, pageable);
        return resultPage.map(MapperUtil::toShipmentResponse);
    }
}
