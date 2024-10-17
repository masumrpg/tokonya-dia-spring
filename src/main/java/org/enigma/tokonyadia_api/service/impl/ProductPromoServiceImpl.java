package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.ProductPromoRequest;
import org.enigma.tokonyadia_api.dto.request.SearchWithMinMaxRequest;
import org.enigma.tokonyadia_api.dto.response.ProductPromoResponse;
import org.enigma.tokonyadia_api.entity.Product;
import org.enigma.tokonyadia_api.entity.ProductPromo;
import org.enigma.tokonyadia_api.repository.ProductPromoRepository;
import org.enigma.tokonyadia_api.service.ProductPromoService;
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

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductPromoServiceImpl implements ProductPromoService {
    private final ProductPromoRepository productPromoRepository;
    private final ProductService productService;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductPromoResponse create(ProductPromoRequest request) {
        validationUtil.validate(request);
        Product product = productService.getOneById(request.getProductId());
        ProductPromo productPromo = ProductPromo.builder()
                .product(product)
                .promoCode(request.getPromoCode().toUpperCase())
                .discountPercentage(request.getDiscount())
                .startDateTime(LocalDateTime.parse(request.getStartDateTime()))
                .endDateTime(LocalDateTime.parse(request.getEndDateTime()))
                .build();
        productPromoRepository.saveAndFlush(productPromo);
        return MapperUtil.toProductPromoResponse(productPromo);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductPromoResponse getById(String promoId) {
        return MapperUtil.toProductPromoResponse(getOneById(promoId));
    }

    @Transactional(readOnly = true)
    @Override
    public ProductPromo getOneById(String promoId) {
        return productPromoRepository.findById(promoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_PRODUCT_PROMO_NOT_FOUND));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductPromoResponse update(String promoId, ProductPromoRequest request) {
        validationUtil.validate(request);
        ProductPromo productPromo = getOneById(promoId);
        Product product = productService.getOneById(request.getProductId());
        productPromo.setProduct(product);
        productPromo.setPromoCode(request.getPromoCode());
        productPromo.setDiscountPercentage(request.getDiscount());
        productPromo.setStartDateTime(LocalDateTime.parse(request.getStartDateTime()));
        productPromo.setEndDateTime(LocalDateTime.parse(request.getEndDateTime()));
        productPromoRepository.save(productPromo);
        return MapperUtil.toProductPromoResponse(productPromo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String promoId) {
        ProductPromo productPromo = getOneById(promoId);
        productPromoRepository.delete(productPromo);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductPromoResponse> getAll(SearchWithMinMaxRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<ProductPromo> specification = new FilterSpecificationBuilder<ProductPromo>()
                .withLike("name", request.getQuery())
                .withGreaterThanOrEqualTo("discountPercentage", request.getMinValue())
                .withLessThanOrEqualTo("discountPercentage", request.getMaxValue())
                .build();
        Page<ProductPromo> resultPage = productPromoRepository.findAll(specification, pageable);
        return resultPage.map(MapperUtil::toProductPromoResponse);
    }
}
