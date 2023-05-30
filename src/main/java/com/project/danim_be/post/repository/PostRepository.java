package com.project.danim_be.post.repository;

import com.project.danim_be.member.entity.Member;
import com.project.danim_be.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, QuerydslPredicateExecutor<Post> {

    List<Post> findAllByMemberOrderByCreatedAt(Member member);

    List<Post> findAllByOrderByCreatedAt(Pageable pageable);
}
