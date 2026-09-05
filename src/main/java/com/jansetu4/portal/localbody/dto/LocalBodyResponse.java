package com.jansetu4.portal.localbody.dto;

import com.jansetu4.portal.common.enums.Domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalBodyResponse {

    private Long id;
    private String name;
    private String jurisdiction;
    private List<Domain> disciplines;
    private String contactEmail;
    private String contactPhone;
}
