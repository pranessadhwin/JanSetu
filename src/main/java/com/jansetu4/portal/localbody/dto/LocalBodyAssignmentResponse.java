package com.jansetu4.portal.localbody.dto;

import com.jansetu4.portal.common.enums.Domain;
import com.jansetu4.portal.common.enums.LocalBodyAssignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalBodyAssignmentResponse {

    private Long id;
    private Long challengeId;
    private String challengeTitle;
    private Domain domain;
    private Long localBodyId;
    private String localBodyName;
    private LocalDateTime assignedAt;
    private LocalBodyAssignmentStatus status;
    private String notes;
    private LocalDateTime resolvedAt;
    private String resolutionNotes;
}
