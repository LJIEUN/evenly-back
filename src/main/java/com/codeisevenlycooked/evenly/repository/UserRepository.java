package com.codeisevenlycooked.evenly.repository;

import com.codeisevenlycooked.evenly.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    // userId 중복체크
    boolean existsByUserId(String userId);

    List<User> findAllByDeletedAtBefore(LocalDateTime now);

}
