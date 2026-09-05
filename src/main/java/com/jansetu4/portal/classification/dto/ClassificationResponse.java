package com.jansetu4.portal.classification.dto;

import com.jansetu4.portal.common.enums.ClassificationMethod;
import com.jansetu4.portal.common.enums.Domain;
import com.jansetu4.portal.common.enums.ResolutionTrack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationResponse {

    private Long id;
    private Long challengeId;
    private Domain domain;
    private LocalDateTime classifiedAt;
    private ClassificationMethod method;
    private ResolutionTrack resolutionTrack;
}
