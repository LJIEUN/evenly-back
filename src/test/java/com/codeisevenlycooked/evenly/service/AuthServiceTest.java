package com.codeisevenlycooked.evenly.service;

import com.codeisevenlycooked.evenly.dto.SignUpDto;
import com.codeisevenlycooked.evenly.entity.User;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private SignUpDto signUpDto;

    @BeforeEach
    void setUp() {
        signUpDto = new SignUpDto("testsignup", "Qwer1234!", "이브니");
    }

    @Test
    void signUp_successes() {
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
    void signUp_fails_id_duplicated() {
        //given
        when(userRepository.existsByUserId(signUpDto.getUserId())).thenReturn(true);

        //when then
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> authService.signUp(signUpDto));

        assertEquals("이미 사용 중인 아이디입니다.", exception.getMessage());

        verify(userRepository, never()).save(any());
    }

}