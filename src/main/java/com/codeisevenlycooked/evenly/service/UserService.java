package com.codeisevenlycooked.evenly.service;

import com.codeisevenlycooked.evenly.config.security.JwtUtil;
import com.codeisevenlycooked.evenly.dto.AdminUserUpdateDto;
import com.codeisevenlycooked.evenly.dto.UpdatePasswordDto;
import com.codeisevenlycooked.evenly.dto.UserInfoDto;
import com.codeisevenlycooked.evenly.entity.User;
import com.codeisevenlycooked.evenly.entity.UserStatus;
import com.codeisevenlycooked.evenly.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

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

    @Transactional
    public void deleteMyAccount(String accessToken) {
        String userId = jwtUtil.getUserIdFromToken(accessToken);
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.deletedAccount();

        redisService.add(accessToken, "BLACKLISTED", 1L);
    }

    /**
     * 6개월 이상 로그인 하지 않은 계정 휴면 처리
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void processInactiveUsers() {
        LocalDateTime inactiveThreshold = LocalDateTime.now().minusMonths(6);

        List<User> inactiveUsers = userRepository.findByLastLoginAtBeforeAndStatus(inactiveThreshold, UserStatus.ACTIVE);
        for (User user : inactiveUsers) {
            user.setStatus(UserStatus.INACTIVE);
        }

        userRepository.saveAll(inactiveUsers);

        log.info("휴면 처리된 회원 ID 목록: {}", inactiveUsers.stream().map(User::getUserId).toList());
    }

    /**
     * 1년 동안 휴면 상태인 계정 삭제
     */
    @Transactional
    @Scheduled(cron = "0 0 1 * * ?")
    public void deleteOldInactiveUsers() {
        LocalDateTime deleteThreshold = LocalDateTime.now().minusYears(1);

        List<User> usersToDelete = userRepository.findByLastLoginAtBeforeAndStatus(deleteThreshold, UserStatus.INACTIVE);
        userRepository.deleteAll(usersToDelete);

        log.info("삭제된 휴면 회원 ID 목록: {}", usersToDelete.stream().map(User::getUserId).toList());
    }

    /**
     * admin
     */

    @Transactional
    public List<User>  getAllUsers() {
        return userRepository.findAll();
    }

    public User getByUserId(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public void updateUser(Long userId, AdminUserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        String encodedPassword = userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isEmpty()
                ? passwordEncoder.encode(userUpdateDto.getPassword())
                : user.getPassword();

        UserStatus updatedStatus = userUpdateDto.getStatus() != null ? userUpdateDto.getStatus() : user.getStatus();
        LocalDateTime updatedDeletedAt = null;
        if (updatedStatus == UserStatus.DELETED) {
            updatedDeletedAt = LocalDateTime.now();
        } else if (updatedStatus == UserStatus.ACTIVE) {
            updatedDeletedAt = null;
        } else {
            updatedDeletedAt = user.getDeletedAt();
        }

        User updatedUser = user.toBuilder()
                .password(encodedPassword)
                .name(userUpdateDto.getName() != null ? userUpdateDto.getName() : user.getName())
                .status(updatedStatus)
                .role(userUpdateDto.getRole() != null ? userUpdateDto.getRole() : user.getRole())
                .deletedAt(updatedDeletedAt)
                .build();

        userRepository.save(updatedUser);
    }


    @Transactional
    public void softDeleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.deletedAccount();
        userRepository.save(user);
    }

    @Transactional
    public void hardDeleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        userRepository.delete(user);
    }

}
