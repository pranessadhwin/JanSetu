package com.jansetu4.portal.localbody.controller;

import com.jansetu4.portal.auth.entity.User;
import com.jansetu4.portal.common.ApiResponse;
import com.jansetu4.portal.localbody.dto.LocalBodyAssignmentResponse;
import com.jansetu4.portal.localbody.dto.LocalBodyResponse;
import com.jansetu4.portal.localbody.dto.ResolveAssignmentRequest;
import com.jansetu4.portal.localbody.service.LocalBodyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LocalBodyController {

    private final LocalBodyService localBodyService;

    @GetMapping("/local-bodies")
    public ResponseEntity<ApiResponse<List<LocalBodyResponse>>> getLocalBodies(
            @RequestParam(required = false) String discipline) {
        return ResponseEntity.ok(ApiResponse.success(
                "Local bodies fetched successfully",
                localBodyService.getLocalBodies(discipline)
        ));
    }

    @GetMapping("/local-body/{id}/assignments")
    @PreAuthorize("hasAnyRole('LOCAL_BODY_ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<LocalBodyAssignmentResponse>>> getAssignments(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                "Assignments fetched successfully",
                localBodyService.getAssignments(id)
        ));
    }

    @PostMapping("/local-body/assignments/{id}/resolve")
    @PreAuthorize("hasRole('LOCAL_BODY_ADMIN')")
    public ResponseEntity<ApiResponse<LocalBodyAssignmentResponse>> resolveAssignment(
            @PathVariable Long id,
            @Valid @RequestBody ResolveAssignmentRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.success(
                "Issue marked as resolved",
                localBodyService.resolveAssignment(id, currentUser.getLocalBodyId(), request)
        ));
    }
}
