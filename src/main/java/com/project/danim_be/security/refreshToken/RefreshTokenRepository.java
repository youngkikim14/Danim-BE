package com.project.danim_be.security.refreshToken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
	Optional<RefreshToken> findByUserId(String userId);
	Optional<RefreshToken> findByUserIdAndProvider(String userId, String provider);

	void deleteByUserId(String userId);
}
