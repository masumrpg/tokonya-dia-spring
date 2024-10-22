package org.enigma.tokonyadia_api.service.impl;

import org.enigma.tokonyadia_api.controller.ProductCategoryController;
import org.enigma.tokonyadia_api.dto.request.ProductCategoryRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.response.ProductCategoryResponse;
import org.enigma.tokonyadia_api.service.ProductCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductCategoryServiceImplTest {

    @Mock
    private ProductCategoryService productCategoryService;

    @InjectMocks
    private ProductCategoryController productCategoryController;

    private ProductCategoryRequest request;
    private ProductCategoryResponse response;

    @BeforeEach
    void setUp() {
        request = ProductCategoryRequest.builder()
                .name("Electronics")
                .build();

        response = ProductCategoryResponse.builder()
                .id("1")
                .name("Electronics")
                .build();
    }

    @Test
    void createProductCategory_ShouldReturnCreated() {
        when(productCategoryService.create(any(ProductCategoryRequest.class))).thenReturn(response);

        ResponseEntity<?> responseEntity = productCategoryController.createProductCategory(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(productCategoryService, times(1)).create(any(ProductCategoryRequest.class));
    }

    @Test
    void getProductCategory_ShouldReturnOk() {
        String categoryId = "1";
        when(productCategoryService.getById(categoryId)).thenReturn(response);

        ResponseEntity<?> responseEntity = productCategoryController.getProductCategory(categoryId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(productCategoryService, times(1)).getById(categoryId);
    }

    @Test
    void updateProductCategory_ShouldReturnOk() {
        String categoryId = "1";
        when(productCategoryService.update(eq(categoryId), any(ProductCategoryRequest.class))).thenReturn(response);

        ResponseEntity<?> responseEntity = productCategoryController.updateProductCategory(categoryId, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(productCategoryService, times(1)).update(eq(categoryId), any(ProductCategoryRequest.class));
    }

    @Test
    void deleteProductCategoryShouldReturnOk() {
        String categoryId = "1";
        doNothing().when(productCategoryService).delete(categoryId);

        ResponseEntity<?> responseEntity = productCategoryController.deleteProductCategory(categoryId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(productCategoryService, times(1)).delete(categoryId);
    }

    @Test
    void getAllProductCategoryShouldReturnOk() {
        List<ProductCategoryResponse> responseList = Arrays.asList(response);
        Pageable pageable = PageRequest.of(0, 10); // page 1 menjadi index 0
        Page<ProductCategoryResponse> page = new PageImpl<>(responseList, pageable, responseList.size());

        when(productCategoryService.getAll(any(SearchCommonRequest.class))).thenReturn(page);

        ResponseEntity<?> responseEntity = productCategoryController.getAllProductCategory(1, 10, "name", "Electronics");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(productCategoryService, times(1)).getAll(any(SearchCommonRequest.class));
    }
}
