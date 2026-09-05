package com.jansetu4.portal.university.service;

import com.jansetu4.portal.citizen.entity.Challenge;
import com.jansetu4.portal.citizen.repository.ChallengeRepository;
import com.jansetu4.portal.common.enums.AssignmentStatus;
import com.jansetu4.portal.common.enums.ChallengeStatus;
import com.jansetu4.portal.common.enums.Domain;
import com.jansetu4.portal.common.exceptions.BadRequestException;
import com.jansetu4.portal.common.exceptions.ResourceNotFoundException;
import com.jansetu4.portal.university.dto.ProposeSolutionRequest;
import com.jansetu4.portal.university.dto.ReassignAssignmentRequest;
import com.jansetu4.portal.university.dto.UniversityAssignmentResponse;
import com.jansetu4.portal.university.dto.UniversityResponse;
import com.jansetu4.portal.university.entity.University;
import com.jansetu4.portal.university.entity.UniversityAssignment;
import com.jansetu4.portal.university.repository.UniversityAssignmentRepository;
import com.jansetu4.portal.university.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final UniversityAssignmentRepository universityAssignmentRepository;
    private final ChallengeRepository challengeRepository;

    @Transactional(readOnly = true)
    public List<UniversityResponse> getUniversities(String discipline) {
        List<University> universities = discipline == null || discipline.isBlank()
                ? universityRepository.findAllByOrderByIdAsc()
                : universityRepository.findByDomain(parseDomain(discipline));
        return universities.stream().map(this::toResponse).toList();
    }

    @Transactional
    public void autoRouteChallenge(Challenge challenge, Domain domain) {
        List<University> matchingUniversities = universityRepository.findByDomain(domain);
        universityAssignmentRepository.deleteByChallengeId(challenge.getId());

        if (matchingUniversities.isEmpty()) {
            challenge.setStatus(ChallengeStatus.CLASSIFIED);
            challengeRepository.save(challenge);
            return;
        }

        long existingAssignments = universityAssignmentRepository.countByDomain(domain);
        University selectedUniversity = matchingUniversities.get((int) (existingAssignments % matchingUniversities.size()));

        UniversityAssignment assignment = UniversityAssignment.builder()
                .challenge(challenge)
                .university(selectedUniversity)
                .assignedAt(LocalDateTime.now())
                .status(AssignmentStatus.PENDING)
                .notes("Auto-routed for domain " + domain.name())
                .build();

        universityAssignmentRepository.save(assignment);
        challenge.setStatus(ChallengeStatus.ASSIGNED);
        challengeRepository.save(challenge);
    }

    @Transactional
    public void clearAssignment(Long challengeId) {
        universityAssignmentRepository.deleteByChallengeId(challengeId);
    }

    @Transactional(readOnly = true)
    public List<UniversityAssignmentResponse> getClaimableForUniversity(Long universityId) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ResourceNotFoundException("University not found"));
        if (university.getDisciplines().isEmpty()) {
            return List.of();
        }
        return universityAssignmentRepository.findClaimableForDomains(university.getDisciplines()).stream()
                .map(this::toAssignmentResponse)
                .toList();
    }

    @Transactional
    public UniversityAssignmentResponse claimAssignment(Long assignmentId, Long universityId) {
        UniversityAssignment assignment = getAssignment(assignmentId);
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ResourceNotFoundException("University not found"));

        if (assignment.getStatus() == AssignmentStatus.ACCEPTED) {
            throw new BadRequestException("This challenge has already been claimed by a university");
        }
        Domain challengeDomain = assignment.getChallenge().getClassification() != null
                ? assignment.getChallenge().getClassification().getDomain()
                : null;
        if (challengeDomain == null || !university.getDisciplines().contains(challengeDomain)) {
            throw new BadRequestException("This challenge's domain does not match your university's disciplines");
        }

        assignment.setUniversity(university);
        assignment.setStatus(AssignmentStatus.ACCEPTED);
        assignment.setAssignedAt(LocalDateTime.now());
        assignment.getChallenge().setStatus(ChallengeStatus.IN_PROGRESS);
        challengeRepository.save(assignment.getChallenge());
        return toAssignmentResponse(universityAssignmentRepository.save(assignment));
    }

    @Transactional
    public UniversityAssignmentResponse proposeSolution(Long assignmentId, Long universityId, ProposeSolutionRequest request) {
        UniversityAssignment assignment = getAssignment(assignmentId);
        if (!assignment.getUniversity().getId().equals(universityId)) {
            throw new BadRequestException("You can only propose a solution for your own university's assignment");
        }
        if (assignment.getStatus() != AssignmentStatus.ACCEPTED) {
            throw new BadRequestException("Only accepted assignments can have a proposed solution");
        }

        assignment.setSolutionTitle(request.getTitle());
        assignment.setSolutionDescription(request.getDescription());
        assignment.setTeamMembers(request.getTeamMembers());
        assignment.setSolutionProposedAt(LocalDateTime.now());
        return toAssignmentResponse(universityAssignmentRepository.save(assignment));
    }

    @Transactional
    public UniversityAssignmentResponse acceptAssignment(Long assignmentId) {
        UniversityAssignment assignment = getAssignment(assignmentId);
        assignment.setStatus(AssignmentStatus.ACCEPTED);
        assignment.getChallenge().setStatus(ChallengeStatus.IN_PROGRESS);
        challengeRepository.save(assignment.getChallenge());
        return toAssignmentResponse(universityAssignmentRepository.save(assignment));
    }

    @Transactional
    public UniversityAssignmentResponse rejectAssignment(Long assignmentId) {
        UniversityAssignment assignment = getAssignment(assignmentId);
        assignment.setStatus(AssignmentStatus.REJECTED);
        assignment.getChallenge().setStatus(ChallengeStatus.CLASSIFIED);
        challengeRepository.save(assignment.getChallenge());
        return toAssignmentResponse(universityAssignmentRepository.save(assignment));
    }

    @Transactional(readOnly = true)
    public List<UniversityAssignmentResponse> getAssignments(Long universityId) {
        return universityAssignmentRepository.findDashboardAssignments(universityId).stream()
                .map(this::toAssignmentResponse)
                .toList();
    }

    @Transactional
    public UniversityAssignmentResponse reassignAssignment(Long assignmentId, ReassignAssignmentRequest request) {
        UniversityAssignment assignment = getAssignment(assignmentId);
        University university = universityRepository.findById(request.getUniversityId())
                .orElseThrow(() -> new ResourceNotFoundException("University not found"));

        assignment.setUniversity(university);
        assignment.setAssignedAt(LocalDateTime.now());
        assignment.setStatus(AssignmentStatus.PENDING);
        if (request.getNotes() != null && !request.getNotes().isBlank()) {
            assignment.setNotes(request.getNotes());
        }
        assignment.getChallenge().setStatus(ChallengeStatus.ASSIGNED);
        challengeRepository.save(assignment.getChallenge());
        return toAssignmentResponse(universityAssignmentRepository.save(assignment));
    }

    private Domain parseDomain(String discipline) {
        try {
            return Domain.valueOf(discipline.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid discipline: " + discipline);
        }
    }

    private UniversityAssignment getAssignment(Long assignmentId) {
        return universityAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("University assignment not found"));
    }

    private UniversityResponse toResponse(University university) {
        return UniversityResponse.builder()
                .id(university.getId())
                .name(university.getName())
                .location(university.getLocation())
                .disciplines(university.getDisciplines())
                .contactEmail(university.getContactEmail())
                .contactPhone(university.getContactPhone())
                .build();
    }

    private UniversityAssignmentResponse toAssignmentResponse(UniversityAssignment assignment) {
        return UniversityAssignmentResponse.builder()
                .id(assignment.getId())
                .challengeId(assignment.getChallenge().getId())
                .challengeTitle(assignment.getChallenge().getTitle())
                .domain(assignment.getChallenge().getClassification() != null ? assignment.getChallenge().getClassification().getDomain() : null)
                .universityId(assignment.getUniversity().getId())
                .universityName(assignment.getUniversity().getName())
                .assignedAt(assignment.getAssignedAt())
                .status(assignment.getStatus())
                .facultyMentor(assignment.getFacultyMentor())
                .notes(assignment.getNotes())
                .solutionTitle(assignment.getSolutionTitle())
                .solutionDescription(assignment.getSolutionDescription())
                .teamMembers(assignment.getTeamMembers())
                .solutionProposedAt(assignment.getSolutionProposedAt())
                .build();
    }
}
