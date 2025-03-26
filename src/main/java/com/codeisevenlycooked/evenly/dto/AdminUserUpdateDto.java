package com.codeisevenlycooked.evenly.dto;

import com.codeisevenlycooked.evenly.entity.UserRole;
import com.codeisevenlycooked.evenly.entity.UserStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminUserUpdateDto {
    private String userId;
    private String password;
    private String name;
    private UserStatus status;
    private UserRole role;
    private LocalDateTime deleteAt;
}
