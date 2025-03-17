package com.codeisevenlycooked.evenly.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpDto {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Pattern(
            regexp = "^[a-z0-9_]{3,20}$",
            message = "아이디는 영문 소문자, 숫자, 특수문자(_)만 사용 가능하며 3~20자여야 합니다."
    )
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[_!@#$%^&*])[A-Za-z\\d_!@#$%^&*]{8,}$",
            message = "비밀번호는 8자 이상이며, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

}
