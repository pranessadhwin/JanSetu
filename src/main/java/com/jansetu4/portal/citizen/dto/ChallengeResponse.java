package com.jansetu4.portal.citizen.dto;

import com.jansetu4.portal.common.enums.ChallengeStatus;
import com.jansetu4.portal.common.enums.Domain;
import com.jansetu4.portal.common.enums.ResolutionTrack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeResponse {

    private Long id;
    private String title;
    private String description;
    private Double latitude;
    private Double longitude;
    private String address;
    private ChallengeStatus status;
    private Long submittedById;
    private String submittedByName;
    private Domain domain;
    private ResolutionTrack resolutionTrack;
    private String routedToType;
    private String routedToName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ChallengeMediaResponse> media;
}
