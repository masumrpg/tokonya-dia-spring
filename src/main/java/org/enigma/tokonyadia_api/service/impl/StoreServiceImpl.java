package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.request.StoreRequest;
import org.enigma.tokonyadia_api.dto.response.StoreResponse;
import org.enigma.tokonyadia_api.entity.Person;
import org.enigma.tokonyadia_api.entity.Store;
import org.enigma.tokonyadia_api.repository.StoreRepository;
import org.enigma.tokonyadia_api.service.PersonService;
import org.enigma.tokonyadia_api.service.StoreService;
import org.enigma.tokonyadia_api.specification.FilterSpecificationBuilder;
import org.enigma.tokonyadia_api.util.MapperUtil;
import org.enigma.tokonyadia_api.util.SortUtil;
import org.enigma.tokonyadia_api.util.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.enigma.tokonyadia_api.util.MapperUtil.toStoreResponse;


@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    private final PersonService personService;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StoreResponse create(StoreRequest request) {
        validationUtil.validate(request);
        Person person = personService.getOne(request.getPersonId());
        Store store = Store.builder()
                .name(request.getName())
                .person(person)
                .phoneNumber(request.getPhoneNumber())
                .siup(request.getSiup())
                .address(request.getAddress())
                .build();
        storeRepository.saveAndFlush(store);
        return toStoreResponse(store);
    }

    @Transactional(readOnly = true)
    @Override
    public StoreResponse getById(String id) {
        return toStoreResponse(getOne(id));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StoreResponse update(String id, StoreRequest request) {
        validationUtil.validate(request);
        Store store = getOne(id);
        store.setName(request.getName());
        store.setPhoneNumber(request.getPhoneNumber());
        store.setSiup(request.getSiup());
        store.setAddress(request.getAddress());
        storeRepository.save(store);
        return toStoreResponse(store);
    }

    @Transactional(readOnly = true)
    @Override
    public Store getOne(String id) {
        return storeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_STORE_NOT_FOUND));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        Store store = getOne(id);
        storeRepository.delete(store);
    }

    @Transactional(readOnly = true)
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
}
