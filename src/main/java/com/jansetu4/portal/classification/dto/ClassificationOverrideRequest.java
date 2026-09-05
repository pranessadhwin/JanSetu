package com.jansetu4.portal.classification.dto;

import com.jansetu4.portal.common.enums.Domain;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationOverrideRequest {

    @NotNull(message = "Domain is required")
    private Domain domain;
}
