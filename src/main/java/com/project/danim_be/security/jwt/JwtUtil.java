package com.project.danim_be.security.jwt;

import com.project.danim_be.security.auth.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

	private static final String BEARER_PREFIX = "Bearer ";
	public static final String ACCESS_KEY = "ACCESS_KEY";
	public static final String REFRESH_KEY = "REFRESH_KEY";
	private static final long ACCESS_TIME = Duration.ofMinutes(60).toMillis();	//60분
//	private static final long ACCESS_TIME = Duration.ofDays(1).toMillis();	//1일
	private static final long REFRESH_TIME = Duration.ofDays(14).toMillis();	//14일

	@Value("${jwt.secret.key}")
	private String secretKey;
	private Key key;
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	private final UserDetailsServiceImpl userDetailsService;
	private final RedisTemplate<String, String> RefreshTokenRedisTemplate;

	@PostConstruct
	public void init() {

		byte[] bytes = Base64.getDecoder().decode(secretKey); //Base64로 인코딩되어 있는 것을, 값을 가져와서(getDecoder()) 디코드하고(decode(secretKey)), byte 배열로 반환
		key = Keys.hmacShaKeyFor(bytes); //반환된 bytes 를 hmacShaKeyFor() 메서드를 사용해서 Key 객체에 넣기

	}

	public TokenDto createAllToken(String userId) {

		return new TokenDto(createToken(userId, "Access"), createToken(userId, "Refresh"));

	}

	public String createToken(String userId, String token) {

		Date date = new Date();
		long tokenType = token.equals("Access") ? ACCESS_TIME : REFRESH_TIME;

		return BEARER_PREFIX +
			Jwts.builder()
				.setSubject(userId) // 정보 저장
				.setExpiration(new Date(date.getTime() + tokenType))
				.setIssuedAt(date) // 토큰 발행 시간 정보
				.signWith(key, signatureAlgorithm) // 사용할 암호화 알고리즘과
				.compact();

	}
	// 토큰 검증
	public boolean validateToken(String token) {

		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token, 만료된 JWT token 입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
		} catch (IllegalArgumentException e) {
			log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
		}
		return false;

	}

	// 토큰에서 사용자 정보 가져오기
	public String getUserInfoFromToken(String token) {

		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();

	}

	// header 토큰을 가져오기
	public String resolveToken(HttpServletRequest request, String token) {

		String tokenName = token.equals("ACCESS_KEY") ? ACCESS_KEY : REFRESH_KEY;
		String bearerToken = request.getHeader(tokenName);

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(7);
		}

		return null;

	}

	// 인증 객체 생성
	public Authentication createAuthentication(String username) {

		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

	}

	public boolean refreshTokenValid(String token) {

		if (!validateToken(token)) return false;
//		Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(getUserInfoFromToken(token));
		String refreshToken = RefreshTokenRedisTemplate.opsForValue().get(getUserInfoFromToken(token));
		return refreshToken != null && token.equals(refreshToken.substring(7));

	}

	public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {

		response.setHeader(ACCESS_KEY, accessToken);

	}

	public long getExpirationTime(String token) {

		// 토큰에서 만료 시간 정보를 추출
		Claims claims = Jwts.parser()
			.setSigningKey(secretKey)
			.parseClaimsJws(token)
			.getBody();

		// 현재 시간과 만료 시간의 차이를 계산하여 반환
		Date expirationDate = claims.getExpiration();
		Date now = new Date();
		return (expirationDate.getTime() - now.getTime()) / 1000;

	}

}
