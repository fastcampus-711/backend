package com.aptner.v3.user.repository;

import com.aptner.v3.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String email);
    Optional<User> findByUsername(String email);
}
