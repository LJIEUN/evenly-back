package com.codeisevenlycooked.evenly.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "MEMBER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length= 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    @Setter
    private UserStatus status = UserStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserRole role = UserRole.USER;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP NULL")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now().withNano(0);

    @Column(columnDefinition = "TIMESTAMP NULL")
    private LocalDateTime deletedAt; // 탈퇴한 경우 삭제 시간 (30일 후 영구 삭제)

    @Column(columnDefinition = "TIMESTAMP NULL")
    private LocalDateTime lastLoginAt;

    public User(String userId, String password, String name, UserRole role) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.status = UserStatus.ACTIVE;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void deletedAccount() {
        this.status = UserStatus.DELETED;
        this.deletedAt = LocalDateTime.now();
    }

    public void restoreAccount() {
        this.status = UserStatus.ACTIVE;
        this.deletedAt = null;
    }

}
