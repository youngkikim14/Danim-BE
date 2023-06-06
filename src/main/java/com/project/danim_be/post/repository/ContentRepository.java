package com.project.danim_be.post.repository;

import java.util.Optional;

import com.project.danim_be.post.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content,Long> {


	void deleteByPostId(Long id);

	Optional<Content> findByPostId(Long id);
}
