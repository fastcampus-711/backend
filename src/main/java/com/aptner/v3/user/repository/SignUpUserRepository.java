package com.aptner.v3.user.repository;

import com.aptner.v3.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SignUpUserRepository extends JpaRepository<UserEntity,Integer> {
    Optional<Object> findByUserName(String username);
}
