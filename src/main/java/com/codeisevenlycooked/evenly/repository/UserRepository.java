package com.codeisevenlycooked.evenly.repository;

import com.codeisevenlycooked.evenly.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // userId 중복체크
    boolean existsByUserId(String userId);

}
