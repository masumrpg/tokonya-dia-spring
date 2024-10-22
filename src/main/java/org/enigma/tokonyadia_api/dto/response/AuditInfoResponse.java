package org.enigma.tokonyadia_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditInfoResponse {
    private String createdBy;
    private String createdDate;
    private String updatedBy;
    private String updatedDate;
}
