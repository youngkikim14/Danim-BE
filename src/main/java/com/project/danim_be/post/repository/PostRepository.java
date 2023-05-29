package com.project.danim_be.post.repository;

import com.project.danim_be.member.entity.Member;
import com.project.danim_be.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByMemberOrderByCreatedAt(Member member);
}
