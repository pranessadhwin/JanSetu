package com.jansetu4.portal.industry.dto;

import com.jansetu4.portal.common.enums.Domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * A university-proposed solution, ready for industry partners to browse and
 * offer to mentor, fund, prototype, or deploy.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableSolutionResponse {

    private Long assignmentId;
    private Long challengeId;
    private String challengeTitle;
    private String challengeDescription;
    private Domain domain;
    private Long universityId;
    private String universityName;
    private String solutionTitle;
    private String solutionDescription;
    private String teamMembers;
    private LocalDateTime solutionProposedAt;
}
