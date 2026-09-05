package com.jansetu4.portal.citizen.controller;

import com.jansetu4.portal.citizen.entity.ChallengeMedia;
import com.jansetu4.portal.citizen.repository.ChallengeMediaRepository;
import com.jansetu4.portal.citizen.service.FileStorageService;
import com.jansetu4.portal.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

    private final ChallengeMediaRepository challengeMediaRepository;
    private final FileStorageService fileStorageService;

    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadMedia(@PathVariable Long id) {
        ChallengeMedia media = challengeMediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found"));

        Resource resource = fileStorageService.load(media.getFileUrl());
        MediaType mediaType = media.getFileType() != null ? MediaType.parseMediaType(media.getFileType()) : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline().filename(resource.getFilename()).build().toString())
                .contentType(mediaType)
                .body(resource);
    }
}
