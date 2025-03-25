package com.codeisevenlycooked.evenly.dto;

import com.codeisevenlycooked.evenly.entity.UserRole;
import com.codeisevenlycooked.evenly.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserInfoDto {
    private String userId;
    private String name;
    private UserStatus status;
    private UserRole role;
    private LocalDateTime createdAt;
}
