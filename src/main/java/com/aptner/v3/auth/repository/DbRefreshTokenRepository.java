package com.aptner.v3.auth.repository;

import com.aptner.v3.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DbRefreshTokenRepository extends JpaRepository<RefreshToken, String> , RefreshTokenRepository {

}
