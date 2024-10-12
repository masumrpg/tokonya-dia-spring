package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.request.StoreRequest;
import org.enigma.tokonyadia_api.dto.response.StoreResponse;
import org.enigma.tokonyadia_api.entity.Store;
import org.enigma.tokonyadia_api.repository.StoreRepository;
import org.enigma.tokonyadia_api.service.StoreService;
import org.enigma.tokonyadia_api.specification.FilterSpecificationBuilder;
import org.enigma.tokonyadia_api.utils.MapperUtil;
import org.enigma.tokonyadia_api.utils.SortUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.enigma.tokonyadia_api.utils.MapperUtil.toStoreResponse;


@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;

    @Override
    public StoreResponse create(StoreRequest storeRequest) {
        verifyBySiup(storeRequest.getSiup());
        verifyByPhoneNumber(storeRequest.getPhoneNumber());
        Store store = Store.builder()
                .name(storeRequest.getName())
                .phoneNumber(storeRequest.getPhoneNumber())
                .siup(storeRequest.getSiup())
                .address(storeRequest.getAddress())
                .build();
        storeRepository.saveAndFlush(store);
        return toStoreResponse(store);
    }

    @Override
    public StoreResponse getById(String id) {
        return toStoreResponse(getOneById(id));
    }

    @Override
    public StoreResponse update(String id, StoreRequest storeRequest) {
        verifyBySiup(storeRequest.getSiup());
        verifyByPhoneNumber(storeRequest.getPhoneNumber());
        Store store = getOneById(id);
        store.setName(storeRequest.getName());
        store.setPhoneNumber(storeRequest.getPhoneNumber());
        store.setSiup(storeRequest.getSiup());
        store.setAddress(storeRequest.getAddress());
        storeRepository.save(store);
        return toStoreResponse(store);
    }

    @Override
    public Store getOneById(String id) {
        Optional<Store> byId = storeRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Store not found");
    }

    @Override
    public void delete(String id) {
        Store store = getOneById(id);
        storeRepository.delete(store);
    }

    @Override
    public Page<StoreResponse> getAll(SearchCommonRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Store> specification = new FilterSpecificationBuilder<Store>()
                .withLike("name", request.getQuery())
                .withEqual("siup", request.getQuery())
                .withEqual("phoneNumber", request.getQuery())
                .build();
        Page<Store> resultPage = storeRepository.findAll(specification, pageable);

        return resultPage.map(MapperUtil::toStoreResponse);
    }

    private void verifyBySiup(String siup) {
        Optional<Store> bySiup = storeRepository.findByPhoneNumber(siup);
        if (bySiup.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Siup with " + siup + " already exist!");
        }
    }

    private void verifyByPhoneNumber(String email) {
        Optional<Store> byPhone = storeRepository.findByPhoneNumber(email);
        if (byPhone.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone whith " + email + " already exist!");
        }
    }
}
