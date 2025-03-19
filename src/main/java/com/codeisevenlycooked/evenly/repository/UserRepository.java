package com.codeisevenlycooked.evenly.repository;

import com.codeisevenlycooked.evenly.entity.User;
import com.codeisevenlycooked.evenly.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    // userId 중복체크
    boolean existsByUserId(String userId);

    List<User> findAllByDeletedAtBefore(LocalDateTime now);

    List<User> findByLastLoginAtBeforeAndStatus(LocalDateTime date, UserStatus status);
}
