package com.codeisevenlycooked.evenly.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtUserInfoDto {
    private final String userId;
    private final String role;
}
