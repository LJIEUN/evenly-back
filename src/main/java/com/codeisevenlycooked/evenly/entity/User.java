package com.codeisevenlycooked.evenly.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "MEMBER")
@Getter
@NoArgsConstructor
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
    private UserStatus status = UserStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @Column(columnDefinition = "TIMESTAMP NULL")
    private LocalDateTime deletedAt; // 탈퇴한 경우 삭제 시간 (30일 후 영구 삭제)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public User(String userId, String password, String name, UserRole role) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.status = UserStatus.ACTIVE;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    public void requestDelete() {
        this.status = UserStatus.DELETED;
        this.deletedAt = LocalDateTime.now();
    }
}
