package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.response.FileInfo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {
    FileInfo storeFile(MultipartFile multipartFile, String prefixDirectory, List<String> contentTypes);

    Resource readFile(String path);

    void deleteFile(String path);
}
