package org.enigma.tokonyadia_api.service.impl;

import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.ProductCategoryRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.response.ProductCategoryResponse;
import org.enigma.tokonyadia_api.entity.ProductCategory;
import org.enigma.tokonyadia_api.repository.ProductCategoryRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.enigma.tokonyadia_api.util.ValidationUtil;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductCategoryServiceImplTest {

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ValidationUtil validationUtil;

    @InjectMocks
    private ProductCategoryServiceImpl productCategoryService;

    @Test
    void testCreate_Success() {
        ProductCategoryRequest request = new ProductCategoryRequest("Beverages", "Drinks");
        ProductCategory productCategory = ProductCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        Mockito.when(productCategoryRepository.saveAndFlush(Mockito.any(ProductCategory.class)))
                .thenReturn(productCategory);

        ProductCategoryResponse response = productCategoryService.create(request);

        assertNotNull(response);
        assertEquals("Beverages", response.getName());
        Mockito.verify(validationUtil).validate(request);
        Mockito.verify(productCategoryRepository).saveAndFlush(Mockito.any(ProductCategory.class));
    }

    @Test
    void testGetById_Success() {
        ProductCategory productCategory = ProductCategory.builder()
                .name("Electronics")
                .description("Gadgets")
                .build();

        Mockito.when(productCategoryRepository.findById("1")).thenReturn(Optional.of(productCategory));

        ProductCategoryResponse response = productCategoryService.getById("1");

        assertNotNull(response);
        assertEquals("Electronics", response.getName());
        Mockito.verify(productCategoryRepository).findById("1");
    }

    @Test
    void testGetById_NotFound() {
        Mockito.when(productCategoryRepository.findById("1")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productCategoryService.getById("1"));
        assertEquals(Constant.PRODUCT_CATEGORY_NOT_FOUND, exception.getReason());
        Mockito.verify(productCategoryRepository).findById("1");
    }

    @Test
    void testUpdate_Success() {
        ProductCategoryRequest request = new ProductCategoryRequest("Electronics", "Updated Description");
        ProductCategory productCategory = ProductCategory.builder()
                .name("Electronics")
                .description("Gadgets")
                .build();

        Mockito.when(productCategoryRepository.findById("1")).thenReturn(Optional.of(productCategory));
        Mockito.when(productCategoryRepository.save(Mockito.any(ProductCategory.class))).thenReturn(productCategory);

        ProductCategoryResponse response = productCategoryService.update("1", request);

        assertNotNull(response);
        assertEquals("Electronics", response.getName());
        assertEquals("Updated Description", response.getDescription());
        Mockito.verify(validationUtil).validate(request);
        Mockito.verify(productCategoryRepository).save(productCategory);
    }

    @Test
    void testDelete_Success() {
        ProductCategory productCategory = ProductCategory.builder().name("Electronics").build();
        Mockito.when(productCategoryRepository.findById("1")).thenReturn(Optional.of(productCategory));

        productCategoryService.delete("1");

        Mockito.verify(productCategoryRepository).delete(productCategory);
    }

    @Test
    void testGetAll_Success() {
        SearchCommonRequest searchRequest = SearchCommonRequest.builder()
                .page(0)
                .size(10)
                .query("Electronics")
                .sortBy("name")
                .build();

        ProductCategory productCategory = ProductCategory.builder()
                .name("Electronics")
                .description("Gadgets")
                .build();

        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), Sort.by("name"));
        Page<ProductCategory> productCategoryPage = new PageImpl<>(Collections.singletonList(productCategory), pageable, 1);

        Mockito.when(productCategoryRepository.findAll(Mockito.any(Specification.class), Mockito.eq(pageable)))
                .thenReturn(productCategoryPage);

        Page<ProductCategoryResponse> responsePage = productCategoryService.getAll(searchRequest);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        Mockito.verify(productCategoryRepository).findAll(Mockito.any(Specification.class), Mockito.eq(pageable));
    }
}