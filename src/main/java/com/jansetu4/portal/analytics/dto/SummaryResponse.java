package com.jansetu4.portal.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SummaryResponse {

    private long totalChallenges;
    private List<String> labels;
    private List<Long> values;
}
