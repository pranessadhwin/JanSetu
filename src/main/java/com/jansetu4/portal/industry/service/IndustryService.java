package com.jansetu4.portal.industry.service;

import com.jansetu4.portal.common.exceptions.BadRequestException;
import com.jansetu4.portal.common.exceptions.ResourceNotFoundException;
import com.jansetu4.portal.industry.dto.AvailableSolutionResponse;
import com.jansetu4.portal.industry.dto.EngageRequest;
import com.jansetu4.portal.industry.dto.EngagementResponse;
import com.jansetu4.portal.industry.entity.Industry;
import com.jansetu4.portal.industry.entity.IndustryEngagement;
import com.jansetu4.portal.industry.repository.IndustryEngagementRepository;
import com.jansetu4.portal.industry.repository.IndustryRepository;
import com.jansetu4.portal.university.entity.UniversityAssignment;
import com.jansetu4.portal.university.repository.UniversityAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndustryService {

    private final UniversityAssignmentRepository universityAssignmentRepository;
    private final IndustryRepository industryRepository;
    private final IndustryEngagementRepository industryEngagementRepository;

    @Transactional(readOnly = true)
    public List<AvailableSolutionResponse> getAvailableSolutions() {
        return universityAssignmentRepository.findWithProposedSolutions().stream()
                .map(this::toAvailableSolutionResponse)
                .toList();
    }

    @Transactional
    public EngagementResponse engage(Long assignmentId, Long industryId, EngageRequest request) {
        UniversityAssignment assignment = universityAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Solution not found"));
        if (assignment.getSolutionProposedAt() == null) {
            throw new BadRequestException("This challenge does not have a proposed solution yet");
        }
        Industry industry = industryRepository.findById(industryId)
                .orElseThrow(() -> new ResourceNotFoundException("Industry profile not found"));

        IndustryEngagement engagement = IndustryEngagement.builder()
                .assignment(assignment)
                .industry(industry)
                .engagementType(request.getEngagementType())
                .notes(request.getNotes())
                .build();

        return toEngagementResponse(industryEngagementRepository.save(engagement));
    }

    @Transactional(readOnly = true)
    public List<EngagementResponse> getMyEngagements(Long industryId) {
        return industryEngagementRepository.findDashboardEngagements(industryId).stream()
                .map(this::toEngagementResponse)
                .toList();
    }

    private AvailableSolutionResponse toAvailableSolutionResponse(UniversityAssignment assignment) {
        return AvailableSolutionResponse.builder()
                .assignmentId(assignment.getId())
                .challengeId(assignment.getChallenge().getId())
                .challengeTitle(assignment.getChallenge().getTitle())
                .challengeDescription(assignment.getChallenge().getDescription())
                .domain(assignment.getChallenge().getClassification() != null
                        ? assignment.getChallenge().getClassification().getDomain() : null)
                .universityId(assignment.getUniversity().getId())
                .universityName(assignment.getUniversity().getName())
                .solutionTitle(assignment.getSolutionTitle())
                .solutionDescription(assignment.getSolutionDescription())
                .teamMembers(assignment.getTeamMembers())
                .solutionProposedAt(assignment.getSolutionProposedAt())
                .build();
    }

    private EngagementResponse toEngagementResponse(IndustryEngagement engagement) {
        return EngagementResponse.builder()
                .id(engagement.getId())
                .assignmentId(engagement.getAssignment().getId())
                .challengeId(engagement.getAssignment().getChallenge().getId())
                .challengeTitle(engagement.getAssignment().getChallenge().getTitle())
                .universityName(engagement.getAssignment().getUniversity().getName())
                .solutionTitle(engagement.getAssignment().getSolutionTitle())
                .industryId(engagement.getIndustry().getId())
                .industryName(engagement.getIndustry().getName())
                .engagementType(engagement.getEngagementType())
                .notes(engagement.getNotes())
                .createdAt(engagement.getCreatedAt())
                .build();
    }
}
