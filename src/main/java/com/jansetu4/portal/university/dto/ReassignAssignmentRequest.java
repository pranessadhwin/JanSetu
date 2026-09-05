package com.jansetu4.portal.university.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReassignAssignmentRequest {

    @NotNull(message = "University id is required")
    private Long universityId;

    private String notes;
}
