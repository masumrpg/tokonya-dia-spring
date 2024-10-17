package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.ProductRatingRequest;
import org.enigma.tokonyadia_api.dto.request.SearchWithMinMaxRequest;
import org.enigma.tokonyadia_api.dto.request.UpdateProductRatingRequest;
import org.enigma.tokonyadia_api.dto.response.ProductRatingResponse;
import org.enigma.tokonyadia_api.entity.ProductRating;
import org.enigma.tokonyadia_api.repository.ProductRatingRepository;
import org.enigma.tokonyadia_api.service.PersonService;
import org.enigma.tokonyadia_api.service.ProductRatingService;
import org.enigma.tokonyadia_api.service.ProductService;
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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductRatingServiceImpl implements ProductRatingService {
    private final ProductRatingRepository productRatingRepository;
    private final PersonService personService;
    private final ProductService productService;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductRatingResponse create(ProductRatingRequest request) {
        validationUtil.validate(request);
        ProductRating productRating = ProductRating.builder()
                .product(productService.getOneById(request.getProductId()))
                .person(personService.getOneById(request.getPersonId()))
                .rating(request.getRating())
                .review(request.getReview())
                .build();
        productRatingRepository.saveAndFlush(productRating);
        return MapperUtil.toProductRatingResponse(productRating);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductRatingResponse getById(String id) {
        return MapperUtil.toProductRatingResponse(getOneById(id));
    }

    @Transactional(readOnly = true)
    @Override
    public ProductRating getOneById(String id) {
        Optional<ProductRating> optionalProductRating = productRatingRepository.findById(id);
        return optionalProductRating.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.PRODUCT_RATING_NOT_FOUND));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductRatingResponse update(String id, UpdateProductRatingRequest request) {
        validationUtil.validate(request);
        ProductRating productRating = getOneById(id);
        productRating.setRating(request.getRating());
        productRating.setReview(request.getReview());
        productRatingRepository.save(productRating);
        return MapperUtil.toProductRatingResponse(productRating);
    }

    @Transactional(readOnly = true)
    @Override
    public void delete(String id) {
        productRatingRepository.delete(getOneById(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductRatingResponse> getAll(SearchWithMinMaxRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<ProductRating> specification = new FilterSpecificationBuilder<ProductRating>()
                .withGreaterThanOrEqualTo("rating", request.getMinValue())
                .withLessThanOrEqualTo("rating", request.getMaxValue())
                .build();
        Page<ProductRating> resultPage = productRatingRepository.findAll(specification, pageable);
        return resultPage.map(MapperUtil::toProductRatingResponse);
    }
}
