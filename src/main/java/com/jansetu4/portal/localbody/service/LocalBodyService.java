package com.jansetu4.portal.localbody.service;

import com.jansetu4.portal.citizen.entity.Challenge;
import com.jansetu4.portal.citizen.repository.ChallengeRepository;
import com.jansetu4.portal.common.enums.ChallengeStatus;
import com.jansetu4.portal.common.enums.Domain;
import com.jansetu4.portal.common.enums.LocalBodyAssignmentStatus;
import com.jansetu4.portal.common.exceptions.BadRequestException;
import com.jansetu4.portal.common.exceptions.ResourceNotFoundException;
import com.jansetu4.portal.localbody.dto.LocalBodyAssignmentResponse;
import com.jansetu4.portal.localbody.dto.LocalBodyResponse;
import com.jansetu4.portal.localbody.dto.ResolveAssignmentRequest;
import com.jansetu4.portal.localbody.entity.LocalBody;
import com.jansetu4.portal.localbody.entity.LocalBodyAssignment;
import com.jansetu4.portal.localbody.repository.LocalBodyAssignmentRepository;
import com.jansetu4.portal.localbody.repository.LocalBodyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LocalBodyService {

    private final LocalBodyRepository localBodyRepository;
    private final LocalBodyAssignmentRepository localBodyAssignmentRepository;
    private final ChallengeRepository challengeRepository;

    @Transactional(readOnly = true)
    public List<LocalBodyResponse> getLocalBodies(String discipline) {
        List<LocalBody> localBodies = discipline == null || discipline.isBlank()
                ? localBodyRepository.findAllByOrderByIdAsc()
                : localBodyRepository.findByDomain(parseDomain(discipline));
        return localBodies.stream().map(this::toResponse).toList();
    }

    /**
     * Auto-routes a routine challenge to a matching local body. Returns false
     * (and leaves the challenge unrouted) if no local body covers this domain,
     * so the caller can fall back to the university/innovation track instead.
     */
    @Transactional
    public boolean autoRouteChallenge(Challenge challenge, Domain domain) {
        List<LocalBody> matchingBodies = localBodyRepository.findByDomain(domain);
        localBodyAssignmentRepository.deleteByChallengeId(challenge.getId());

        if (matchingBodies.isEmpty()) {
            return false;
        }

        long existingAssignments = localBodyAssignmentRepository.countByDomain(domain);
        LocalBody selectedBody = matchingBodies.get((int) (existingAssignments % matchingBodies.size()));

        LocalBodyAssignment assignment = LocalBodyAssignment.builder()
                .challenge(challenge)
                .localBody(selectedBody)
                .assignedAt(LocalDateTime.now())
                .status(LocalBodyAssignmentStatus.PENDING)
                .notes("Auto-routed as a routine issue for domain " + domain.name())
                .build();

        localBodyAssignmentRepository.save(assignment);
        challenge.setStatus(ChallengeStatus.ASSIGNED);
        challengeRepository.save(challenge);
        return true;
    }

    @Transactional
    public void clearAssignment(Long challengeId) {
        localBodyAssignmentRepository.deleteByChallengeId(challengeId);
    }

    @Transactional(readOnly = true)
    public List<LocalBodyAssignmentResponse> getAssignments(Long localBodyId) {
        return localBodyAssignmentRepository.findDashboardAssignments(localBodyId).stream()
                .map(this::toAssignmentResponse)
                .toList();
    }

    @Transactional
    public LocalBodyAssignmentResponse resolveAssignment(Long assignmentId, Long localBodyId, ResolveAssignmentRequest request) {
        LocalBodyAssignment assignment = localBodyAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Local body assignment not found"));
        if (!assignment.getLocalBody().getId().equals(localBodyId)) {
            throw new BadRequestException("You can only resolve your own local body's assignments");
        }
        if (assignment.getStatus() == LocalBodyAssignmentStatus.RESOLVED) {
            throw new BadRequestException("This issue has already been resolved");
        }

        assignment.setStatus(LocalBodyAssignmentStatus.RESOLVED);
        assignment.setResolvedAt(LocalDateTime.now());
        assignment.setResolutionNotes(request.getResolutionNotes());
        assignment.getChallenge().setStatus(ChallengeStatus.RESOLVED);
        challengeRepository.save(assignment.getChallenge());
        return toAssignmentResponse(localBodyAssignmentRepository.save(assignment));
    }

    private Domain parseDomain(String discipline) {
        try {
            return Domain.valueOf(discipline.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid discipline: " + discipline);
        }
    }

    private LocalBodyResponse toResponse(LocalBody localBody) {
        return LocalBodyResponse.builder()
                .id(localBody.getId())
                .name(localBody.getName())
                .jurisdiction(localBody.getJurisdiction())
                .disciplines(localBody.getDisciplines())
                .contactEmail(localBody.getContactEmail())
                .contactPhone(localBody.getContactPhone())
                .build();
    }

    private LocalBodyAssignmentResponse toAssignmentResponse(LocalBodyAssignment assignment) {
        return LocalBodyAssignmentResponse.builder()
                .id(assignment.getId())
                .challengeId(assignment.getChallenge().getId())
                .challengeTitle(assignment.getChallenge().getTitle())
                .domain(assignment.getChallenge().getClassification() != null
                        ? assignment.getChallenge().getClassification().getDomain() : null)
                .localBodyId(assignment.getLocalBody().getId())
                .localBodyName(assignment.getLocalBody().getName())
                .assignedAt(assignment.getAssignedAt())
                .status(assignment.getStatus())
                .notes(assignment.getNotes())
                .resolvedAt(assignment.getResolvedAt())
                .resolutionNotes(assignment.getResolutionNotes())
                .build();
    }
}
