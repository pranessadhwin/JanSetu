package com.jansetu4.portal.admin.service;

import com.jansetu4.portal.admin.dto.PendingUserResponse;
import com.jansetu4.portal.auth.entity.User;
import com.jansetu4.portal.auth.repository.UserRepository;
import com.jansetu4.portal.common.enums.Role;
import com.jansetu4.portal.common.exceptions.BadRequestException;
import com.jansetu4.portal.common.exceptions.ResourceNotFoundException;
import com.jansetu4.portal.industry.repository.IndustryRepository;
import com.jansetu4.portal.university.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;
    private final IndustryRepository industryRepository;

    @Transactional(readOnly = true)
    public List<PendingUserResponse> getPendingUsers() {
        return userRepository.findAllByApprovedFalseOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void approveUser(Long userId) {
        User user = getUser(userId);
        user.setApproved(true);
        userRepository.save(user);
    }

    @Transactional
    public void rejectUser(Long userId) {
        User user = getUser(userId);
        if (user.isApproved()) {
            throw new BadRequestException("This account is already approved");
        }
        userRepository.delete(user);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private PendingUserResponse toResponse(User user) {
        String organizationName = null;
        if (user.getRole() == Role.UNIVERSITY_ADMIN && user.getUniversityId() != null) {
            organizationName = universityRepository.findById(user.getUniversityId())
                    .map(u -> u.getName()).orElse(null);
        } else if (user.getRole() == Role.INDUSTRY && user.getIndustryId() != null) {
            organizationName = industryRepository.findById(user.getIndustryId())
                    .map(i -> i.getName()).orElse(null);
        }

        return PendingUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .organizationName(organizationName)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
