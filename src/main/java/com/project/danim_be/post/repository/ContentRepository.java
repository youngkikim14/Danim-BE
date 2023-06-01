package com.project.danim_be.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.danim_be.post.entity.Content;

public interface ContentRepository extends JpaRepository<Content,Long> {


	void deleteByPostId(Long id);

}
