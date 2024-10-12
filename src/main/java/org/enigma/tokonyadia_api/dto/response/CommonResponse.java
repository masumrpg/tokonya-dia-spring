package org.enigma.tokonyadia_api.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CommonResponse<T> {
    private Integer status;
    private String message;
    private T data;
}

