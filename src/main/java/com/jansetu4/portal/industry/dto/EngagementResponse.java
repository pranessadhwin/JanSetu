package com.jansetu4.portal.industry.dto;

import com.jansetu4.portal.common.enums.EngagementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngagementResponse {

    private Long id;
    private Long assignmentId;
    private Long challengeId;
    private String challengeTitle;
    private String universityName;
    private String solutionTitle;
    private Long industryId;
    private String industryName;
    private EngagementType engagementType;
    private String notes;
    private LocalDateTime createdAt;
}
