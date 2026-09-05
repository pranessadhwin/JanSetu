package com.jansetu4.portal.industry.controller;

import com.jansetu4.portal.auth.AuthService;
import com.jansetu4.portal.auth.dto.IndustryRegisterRequest;
import com.jansetu4.portal.auth.entity.User;
import com.jansetu4.portal.common.ApiResponse;
import com.jansetu4.portal.industry.dto.AvailableSolutionResponse;
import com.jansetu4.portal.industry.dto.EngageRequest;
import com.jansetu4.portal.industry.dto.EngagementResponse;
import com.jansetu4.portal.industry.service.IndustryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/industry")
@RequiredArgsConstructor
public class IndustryController {

    private final IndustryService industryService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> registerIndustry(@Valid @RequestBody IndustryRegisterRequest request) {
        authService.registerIndustry(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Registration submitted. A Super Admin must approve your account before you can log in.", null));
    }

    @GetMapping("/solutions/available")
    @PreAuthorize("hasAnyRole('INDUSTRY','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<AvailableSolutionResponse>>> getAvailableSolutions() {
        return ResponseEntity.ok(ApiResponse.success(
                "Available solutions fetched successfully", industryService.getAvailableSolutions()));
    }

    @PostMapping("/solutions/{assignmentId}/engage")
    @PreAuthorize("hasRole('INDUSTRY')")
    public ResponseEntity<ApiResponse<EngagementResponse>> engage(
            @PathVariable Long assignmentId,
            @Valid @RequestBody EngageRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Engagement recorded successfully",
                industryService.engage(assignmentId, currentUser.getIndustryId(), request)
        ));
    }

    @GetMapping("/engagements/my")
    @PreAuthorize("hasRole('INDUSTRY')")
    public ResponseEntity<ApiResponse<List<EngagementResponse>>> getMyEngagements(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.success(
                "Engagements fetched successfully",
                industryService.getMyEngagements(currentUser.getIndustryId())
        ));
    }
}
