package org.enigma.tokonyadia_api.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CommonWithPagingResponse<T> {
    private Integer status;
    private String message;
    private T data;
    private PagingResponse paging;
}

