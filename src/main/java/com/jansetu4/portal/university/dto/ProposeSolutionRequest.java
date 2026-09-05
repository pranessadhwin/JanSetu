package com.jansetu4.portal.university.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposeSolutionRequest {

    @NotBlank(message = "Solution title is required")
    private String title;

    @NotBlank(message = "Solution description is required")
    private String description;

    private String teamMembers;
}
