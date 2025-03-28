package com.codeisevenlycooked.evenly.controller;

import com.codeisevenlycooked.evenly.config.security.JwtUtil;
import com.codeisevenlycooked.evenly.dto.UpdatePasswordDto;
import com.codeisevenlycooked.evenly.dto.UserInfoDto;
import com.codeisevenlycooked.evenly.global.MessageResponse;
import com.codeisevenlycooked.evenly.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
        String accessToken = jwtUtil.resolveToken(token);

        UserInfoDto userInfo = userService.getMyInfo(accessToken);
        return ResponseEntity.ok(userInfo);
    }

    @PatchMapping("/my")
    public ResponseEntity<MessageResponse> updatePassword(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UpdatePasswordDto updatePasswordDto) {

        String accessToken = jwtUtil.resolveToken(token);
        userService.updatePassword(accessToken, updatePasswordDto);

        return ResponseEntity.ok(new MessageResponse("비밀번호가 성공적으로 변경되었습니다."));
    }

    @DeleteMapping("/my")
    public ResponseEntity<MessageResponse> deleteMyAccount(@RequestHeader("Authorization") String token) {
        String accessToken = jwtUtil.resolveToken(token);
        userService.deleteMyAccount(accessToken);
        return ResponseEntity.ok(new MessageResponse("회원 탈퇴가 완료되었습니다. 회원 정보가 30일 후에 완전 삭제됩니다."));
    }
}
