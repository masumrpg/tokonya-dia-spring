package org.enigma.tokonyadia_api.dto.response;

import lombok.*;
import org.springframework.core.io.Resource;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDownloadResponse {
    private Resource resource;
    private String contentType;
}