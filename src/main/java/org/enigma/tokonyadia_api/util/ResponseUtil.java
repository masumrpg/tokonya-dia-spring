package org.enigma.tokonyadia_api.util;

import org.enigma.tokonyadia_api.dto.response.CommonResponse;
import org.enigma.tokonyadia_api.dto.response.CommonWithPagingResponse;
import org.enigma.tokonyadia_api.dto.response.PagingResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseUtil {
    public static <T> ResponseEntity<CommonResponse<T>> buildCommonResponse(HttpStatus httpStatus, String message, T data) {
        CommonResponse<T> commonResponse = new CommonResponse<>(httpStatus.value(), message, data);
        return ResponseEntity.status(httpStatus).body(commonResponse);
    }

    public static <T> ResponseEntity<CommonWithPagingResponse<?>> buildResponsePage(
            HttpStatus httpStatus,
            String message,
            Page<T> page
    ) {
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .page(page.getPageable().getPageNumber() + 1)
                .size(page.getSize())
                .build();

        CommonWithPagingResponse<List<T>> response = new CommonWithPagingResponse<>(
                httpStatus.value(),
                message,
                page.getContent(),
                pagingResponse
        );
        return ResponseEntity.status(httpStatus).body(response);
    }
}

