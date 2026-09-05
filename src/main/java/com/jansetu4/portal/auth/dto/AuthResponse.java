package com.jansetu4.portal.auth.dto;

import com.jansetu4.portal.common.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private Long userId;
    private String name;
    private String email;
    private Role role;
    private Long universityId;
    private Long industryId;
}
