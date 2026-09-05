package com.jansetu4.portal.admin.controller;

import com.jansetu4.portal.admin.dto.PendingUserResponse;
import com.jansetu4.portal.admin.service.AdminService;
import com.jansetu4.portal.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users/pending")
    public ResponseEntity<ApiResponse<List<PendingUserResponse>>> getPendingUsers() {
        return ResponseEntity.ok(ApiResponse.success("Pending users fetched successfully", adminService.getPendingUsers()));
    }

    @PostMapping("/users/{id}/approve")
    public ResponseEntity<ApiResponse<Object>> approveUser(@PathVariable Long id) {
        adminService.approveUser(id);
        return ResponseEntity.ok(ApiResponse.success("User approved successfully", null));
    }

    @PostMapping("/users/{id}/reject")
    public ResponseEntity<ApiResponse<Object>> rejectUser(@PathVariable Long id) {
        adminService.rejectUser(id);
        return ResponseEntity.ok(ApiResponse.success("User rejected successfully", null));
    }
}
