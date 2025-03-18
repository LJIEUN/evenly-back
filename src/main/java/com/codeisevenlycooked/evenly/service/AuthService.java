package com.codeisevenlycooked.evenly.service;

import com.codeisevenlycooked.evenly.dto.SignUpDto;
import com.codeisevenlycooked.evenly.entity.User;
import com.codeisevenlycooked.evenly.entity.UserRole;
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

}
