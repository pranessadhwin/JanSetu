package com.jansetu4.portal.industry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndustryResponse {

    private Long id;
    private String name;
    private String sector;
    private String contactEmail;
    private String contactPhone;
}
