package com.project.danim_be.member.repository;


import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.danim_be.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByUserId(String userId);

	Optional<Member> findByNickname(String nickname);


}
