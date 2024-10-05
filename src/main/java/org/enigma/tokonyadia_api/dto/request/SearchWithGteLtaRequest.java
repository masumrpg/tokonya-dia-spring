package org.enigma.tokonyadia_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SearchWithGteLtaRequest extends SearchCommonRequest {
    private Long minValue;
    private Long maxValue;
}
