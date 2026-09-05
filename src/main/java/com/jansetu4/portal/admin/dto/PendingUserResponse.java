package com.jansetu4.portal.admin.dto;

import com.jansetu4.portal.common.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingUserResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private Role role;
    private String organizationName;
    private LocalDateTime createdAt;
}
