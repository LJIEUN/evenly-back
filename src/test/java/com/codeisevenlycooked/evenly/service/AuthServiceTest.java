package com.codeisevenlycooked.evenly.service;

import com.codeisevenlycooked.evenly.config.security.JwtUtil;
import com.codeisevenlycooked.evenly.dto.SignInDto;
import com.codeisevenlycooked.evenly.dto.SignUpDto;
import com.codeisevenlycooked.evenly.entity.User;
import com.codeisevenlycooked.evenly.entity.UserRole;
import com.codeisevenlycooked.evenly.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private SignUpDto signUpDto;
    private SignInDto signInDto;
    private User testUser;

    @BeforeEach
    void setUp() {
        signUpDto = new SignUpDto("testsignup", "Qwer1234!", "이브니");
        signInDto = new SignInDto("testsignup", "Qwer1234!");
        testUser = User.builder()
                .userId("testsignup")
                .password("틀린비밀번호")
                .name("이브니")
                .role(UserRole.USER)
                .build();
    }

    @Test
    void signUp_success() {
        //given
        when(userRepository.existsByUserId(signUpDto.getUserId())).thenReturn(false);
        when(passwordEncoder.encode(signUpDto.getPassword())).thenReturn("encodedPassword");
        //when
        authService.signUp(signUpDto);
        //then
        ArgumentCaptor<User> uc = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(uc.capture());

        User savedUser = uc.getValue();
        assertEquals("testsignup", savedUser.getUserId());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("이브니", savedUser.getName());
    }

    @Test
    void signUp_fail_id_duplicated() {
        //given
        when(userRepository.existsByUserId(signUpDto.getUserId())).thenReturn(true);

        //when then
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> authService.signUp(signUpDto));

        assertEquals("이미 사용 중인 아이디입니다.", exception.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_success() {
        when(userRepository.findByUserId(signUpDto.getUserId())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(signInDto.getPassword(), testUser.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(any())).thenReturn(new String[]{"accessToken", "refreshToken"});

        String[] tokens = authService.login(signInDto);

        assertNotNull(tokens);
        assertEquals(2, tokens.length);
        assertEquals("accessToken", tokens[0]);
        assertEquals("refreshToken", tokens[1]);
    }

    @Test
    void login_userNotFound() {
        when(userRepository.findByUserId(signUpDto.getUserId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> authService.login(signInDto));
        assertEquals("존재하지 않는 사용자입니다.", exception.getMessage());
    }

    @Test
    void login_wrongPassword() {
        when(userRepository.findByUserId(signInDto.getUserId())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(signInDto.getPassword(), testUser.getPassword())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> authService.login(signInDto));
        assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
    }
}