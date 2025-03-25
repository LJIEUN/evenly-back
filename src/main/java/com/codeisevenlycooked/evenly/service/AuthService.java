package com.codeisevenlycooked.evenly.service;

import com.codeisevenlycooked.evenly.config.security.JwtUtil;
import com.codeisevenlycooked.evenly.dto.JwtUserInfoDto;
import com.codeisevenlycooked.evenly.dto.SignInDto;
import com.codeisevenlycooked.evenly.dto.SignUpDto;
import com.codeisevenlycooked.evenly.entity.User;
import com.codeisevenlycooked.evenly.entity.UserRole;
import com.codeisevenlycooked.evenly.entity.UserStatus;
import com.codeisevenlycooked.evenly.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signUp(SignUpDto signUpDto) {
        //userId 중복 검사
        if (userRepository.existsByUserId(signUpDto.getUserId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        //비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());

        User user = new User(signUpDto.getUserId(), encodedPassword, signUpDto.getName(), UserRole.USER);

        userRepository.save(user);
    }

    public String[] login(SignInDto signInDto) {
        User user = userRepository.findByUserId(signInDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (user.getStatus() == UserStatus.BANNED || user.getStatus() == UserStatus.DELETED) {
            throw new IllegalArgumentException("탈퇴한 회원은 로그인할 수 없습니다.");
        }

        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        //마지막 로그인 시간 업데이트
        user.updateLastLogin();
        userRepository.save(user);

        //Jwt 발급
        JwtUserInfoDto userInfo = new JwtUserInfoDto(user.getUserId(), user.getRole().name());
        return jwtUtil.generateToken(userInfo);
    }

}
