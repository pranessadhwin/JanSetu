package com.jansetu4.portal.industry.dto;

import com.jansetu4.portal.common.enums.EngagementType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngageRequest {

    @NotNull(message = "Engagement type is required")
    private EngagementType engagementType;

    private String notes;
}
