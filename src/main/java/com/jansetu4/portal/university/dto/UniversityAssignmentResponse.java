package com.jansetu4.portal.university.dto;

import com.jansetu4.portal.common.enums.AssignmentStatus;
import com.jansetu4.portal.common.enums.Domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniversityAssignmentResponse {

    private Long id;
    private Long challengeId;
    private String challengeTitle;
    private Domain domain;
    private Long universityId;
    private String universityName;
    private LocalDateTime assignedAt;
    private AssignmentStatus status;
    private String facultyMentor;
    private String notes;
    private String solutionTitle;
    private String solutionDescription;
    private String teamMembers;
    private LocalDateTime solutionProposedAt;
}
