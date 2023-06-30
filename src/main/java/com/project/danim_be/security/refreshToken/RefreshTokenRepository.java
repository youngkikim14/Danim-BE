package com.project.danim_be.security.refreshToken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

	Optional<RefreshToken> findByUserId(String userId);
	Optional<RefreshToken> findByUserIdAndProvider(String userId, String provider);
	List<RefreshToken> findAllByUserId(String userId);

}
