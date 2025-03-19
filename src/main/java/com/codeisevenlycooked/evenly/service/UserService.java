package com.codeisevenlycooked.evenly.service;

import com.codeisevenlycooked.evenly.config.security.JwtUtil;
import com.codeisevenlycooked.evenly.dto.UpdatePasswordDto;
import com.codeisevenlycooked.evenly.dto.UserInfoDto;
import com.codeisevenlycooked.evenly.entity.User;
import com.codeisevenlycooked.evenly.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserInfoDto getMyInfo(String accessToken) {
        String userId = jwtUtil.getUserIdFromToken(accessToken);
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new UserInfoDto(user.getUserId(), user.getName(), user.getStatus(), user.getRole(), user.getCreatedAt());
    }

    @Transactional
    public void updatePassword(String accessToken, UpdatePasswordDto updatePasswordDto) {
        String userId = jwtUtil.getUserIdFromToken(accessToken);
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        //현재 비밀번호 검증
        if (!passwordEncoder.matches(updatePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(updatePasswordDto.getNewPassword());
        user.updatePassword(encodedPassword);
    }

}
