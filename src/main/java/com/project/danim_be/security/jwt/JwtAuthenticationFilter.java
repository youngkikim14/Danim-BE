package com.project.danim_be.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final MemberRepository memberRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException,ServletException {

		String access_token  = jwtUtil.resolveToken(request, JwtUtil.ACCESS_KEY);
		String refresh_token = jwtUtil.resolveToken(request, JwtUtil.REFRESH_KEY);

		if(access_token == null) {
			filterChain.doFilter(request, response);
		} else {
			if(jwtUtil.validateToken(access_token)) {
				setAuthentication(jwtUtil.getUserInfoFromToken(access_token));
			} else if(refresh_token != null && jwtUtil.refreshTokenValid(refresh_token)) {
				String userId = jwtUtil.getUserInfoFromToken(refresh_token);
				Member member = memberRepository.findByUserId(userId).orElseThrow();
				String newAccessToken = jwtUtil.createToken(userId, "Access");
				jwtUtil.setCookieAccessToken(response, newAccessToken);
				setAuthentication(userId);
			} else if(refresh_token == null) {
				jwtExceptionHandler(response, "AccessToken Expired.", HttpStatus.BAD_REQUEST.value());
				return;
			} else {
				jwtExceptionHandler(response, "RefreshToken Expired.", HttpStatus.BAD_REQUEST.value());
				return ;
			}
			filterChain.doFilter(request, response);
		}

	}


	public void setAuthentication(String userId) {

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = jwtUtil.createAuthentication(userId);
		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);

	}


	public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {

		response.setStatus(statusCode);
		response.setContentType("application/json");
		try {
			String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
			response.getWriter().write(json);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}

