package com.jansetu4.portal.citizen.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String store(MultipartFile file, String subDir);

    Resource load(String url);
}
