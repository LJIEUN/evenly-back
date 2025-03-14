package com.codeisevenlycooked.evenly.mock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/mock/users")
public class MockUserController {

    private MockUserResponse mockUser = new MockUserResponse(1L, "evenie", "이브니", "ACTIVE", LocalDateTime.now());

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<MockUserResponse> signup(@RequestBody MockUserSignupRequest request) {
        log.info("회원가입 요청: {}", request);
        mockUser = new MockUserResponse(2L, request.getUserId(), request.getName(), "ACTIVE", LocalDateTime.now());
        return ResponseEntity.ok(mockUser);
    }

    // 내 정보 조회 (마이페이지)
    @GetMapping("/my")
    public ResponseEntity<MockUserResponse> getMyInfo() {
        return ResponseEntity.ok(mockUser);
    }

    // 내 정보 수정 (비밀번호만 변경 가능)
    @PatchMapping("/my")
    public ResponseEntity<MockUserResponse> updateMyInfo(@RequestBody MockUserUpdateRequest request) {
        log.info("회원 정보 수정 요청: {}", request);
        return ResponseEntity.ok(mockUser);
    }
}

// 회원가입 요청 DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
class MockUserSignupRequest {
    private String userId;
    private String name;
    private String password;
}

// 회원 정보 수정 요청 DTO (비밀번호만 수정 가능)
@Data
@AllArgsConstructor
@NoArgsConstructor
class MockUserUpdateRequest {
    private String password;
}

// 회원 응답 DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
class MockUserResponse {
    private Long id;
    private String userId;
    private String name;
    private String status;
    private LocalDateTime createdAt;
}