package org.enigma.tokonyadia_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.dto.response.FileDownloadResponse;
import org.enigma.tokonyadia_api.service.ProductImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "File Management", description = "APIs for managing file uploads and downloads")
public class FileController {

    private final ProductImageService productImageService;

    @Operation(summary = "Download Image", description = "Download an image file by its ID")
    @GetMapping(path = "/images/{id}")
    public ResponseEntity<?> downloadImage(@PathVariable String id) {
        FileDownloadResponse response = productImageService.downloadImage(id);
        String headerValue = String.format("inline; filename=%s", response.getResource().getFilename());
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .contentType(MediaType.valueOf(response.getContentType()))
                .body(response.getResource());
    }
}
