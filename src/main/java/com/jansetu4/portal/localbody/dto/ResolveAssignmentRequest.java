package com.jansetu4.portal.localbody.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResolveAssignmentRequest {

    @NotBlank(message = "Resolution notes are required")
    private String resolutionNotes;
}
