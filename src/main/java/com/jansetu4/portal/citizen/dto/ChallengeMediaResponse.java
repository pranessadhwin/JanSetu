package com.jansetu4.portal.citizen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeMediaResponse {

    private Long id;
    private String fileUrl;
    private String fileType;
}
