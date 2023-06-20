package com.project.danim_be.post.repository;

import com.project.danim_be.member.entity.Member;
import com.project.danim_be.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, QuerydslPredicateExecutor<Post> {

    List<Post> findAllByMemberOrderByCreatedAtDesc(Member member);

    List<Post> findAllByOrderByCreatedAt(Pageable pageable);

    List<Post> findByMember_Id(Long id);

    Optional<Post> findByChatRoom_Id(Long roomId);
}
