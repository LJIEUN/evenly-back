package com.codeisevenlycooked.evenly.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdatePasswordDto {
    @NotBlank
    private String currentPassword;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[_!@#$%^&*])[A-Za-z\\d_!@#$%^&*]{8,}$",
            message = "비밀번호는 8자 이상이며, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private String newPassword;
}
