package com.jansetu4.portal.citizen.service;

import com.jansetu4.portal.auth.entity.User;
import com.jansetu4.portal.citizen.dto.ChallengeMediaResponse;
import com.jansetu4.portal.citizen.dto.ChallengeResponse;
import com.jansetu4.portal.citizen.dto.CreateChallengeRequest;
import com.jansetu4.portal.citizen.entity.Challenge;
import com.jansetu4.portal.citizen.entity.ChallengeMedia;
import com.jansetu4.portal.citizen.repository.ChallengeRepository;
import com.jansetu4.portal.classification.entity.Classification;
import com.jansetu4.portal.classification.service.ClassificationService;
import com.jansetu4.portal.common.enums.ChallengeStatus;
import com.jansetu4.portal.common.enums.Role;
import com.jansetu4.portal.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final FileStorageService fileStorageService;
    private final ClassificationService classificationService;

    @Transactional
    public ChallengeResponse createChallenge(CreateChallengeRequest request, User currentUser) {
        Challenge challenge = Challenge.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .submittedBy(currentUser)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .address(request.getAddress())
                .status(ChallengeStatus.SUBMITTED)
                .build();

        Challenge savedChallenge = challengeRepository.save(challenge);

        for (MultipartFile file : request.getFiles()) {
            if (file != null && !file.isEmpty()) {
                ChallengeMedia media = ChallengeMedia.builder()
                        .challenge(savedChallenge)
                        .fileUrl(fileStorageService.store(file, "challenges"))
                        .fileType(file.getContentType())
                        .build();
                savedChallenge.getMedia().add(media);
            }
        }

        challengeRepository.save(savedChallenge);
        classificationService.classify(savedChallenge);
        return getChallengeDetail(savedChallenge.getId(), currentUser);
    }

    @Transactional(readOnly = true)
    public List<ChallengeResponse> getMyChallenges(User currentUser) {
        return challengeRepository.findAllDetailedBySubmittedById(currentUser.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ChallengeResponse getChallengeDetail(Long challengeId, User currentUser) {
        Challenge challenge = challengeRepository.findDetailedById(challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge not found"));

        boolean isAdmin = currentUser.getRole() == Role.SUPER_ADMIN || currentUser.getRole() == Role.UNIVERSITY_ADMIN;
        boolean isOwner = challenge.getSubmittedBy().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("You are not allowed to access this challenge");
        }

        return toResponse(challenge);
    }

    private ChallengeResponse toResponse(Challenge challenge) {
        Classification classification = challenge.getClassification();
        return ChallengeResponse.builder()
                .id(challenge.getId())
                .title(challenge.getTitle())
                .description(challenge.getDescription())
                .latitude(challenge.getLatitude())
                .longitude(challenge.getLongitude())
                .address(challenge.getAddress())
                .status(challenge.getStatus())
                .submittedById(challenge.getSubmittedBy().getId())
                .submittedByName(challenge.getSubmittedBy().getName())
                .domain(classification != null ? classification.getDomain() : null)
                .confidenceScore(classification != null ? classification.getConfidenceScore() : null)
                .createdAt(challenge.getCreatedAt())
                .updatedAt(challenge.getUpdatedAt())
                .media(challenge.getMedia().stream()
                        .map(media -> ChallengeMediaResponse.builder()
                                .id(media.getId())
                                .fileUrl(media.getFileUrl())
                                .fileType(media.getFileType())
                                .build())
                        .toList())
                .build();
    }
}
