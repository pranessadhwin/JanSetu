package com.jansetu4.portal.classification.controller;

import com.jansetu4.portal.classification.dto.ClassificationOverrideRequest;
import com.jansetu4.portal.classification.dto.ClassificationResponse;
import com.jansetu4.portal.classification.service.ClassificationService;
import com.jansetu4.portal.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/classification")
@RequiredArgsConstructor
public class ClassificationController {

    private final ClassificationService classificationService;

    @PostMapping("/{challengeId}/override")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ClassificationResponse>> overrideClassification(
            @PathVariable Long challengeId,
            @Valid @RequestBody ClassificationOverrideRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                "Classification overridden successfully",
                classificationService.overrideClassification(challengeId, request.getDomain())
        ));
    }
}
