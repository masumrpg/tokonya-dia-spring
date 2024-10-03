package org.enigma.tokonyadia_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
// T -> Type non primitive
// U, V -> Pembuatan generic type untuk 2nd
// E -> Element dari suatu collection
// R -> Return type
public class CommonResponse<T> {
    private Integer status;
    private String message;
    private T data;
    private PagingResponse paging;
}

