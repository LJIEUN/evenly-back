package com.codeisevenlycooked.evenly.controller;

import com.codeisevenlycooked.evenly.config.security.JwtUtil;
import com.codeisevenlycooked.evenly.dto.UpdatePasswordDto;
import com.codeisevenlycooked.evenly.dto.UserInfoDto;
import com.codeisevenlycooked.evenly.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/my")
    public ResponseEntity<UserInfoDto> getMyInfo(@RequestHeader("Authorization") String token) {
        String accessToken = token.replace("Bearer ", "");
        UserInfoDto userInfo = userService.getMyInfo(accessToken);
        return ResponseEntity.ok(userInfo);
    }

    @PatchMapping("/my")
    public ResponseEntity<String> updatePassword(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UpdatePasswordDto updatePasswordDto) {

        String accessToken = token.replace("Bearer ", "");
        userService.updatePassword(accessToken, updatePasswordDto);

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }
}
