package com.project.danim_be.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.danim_be.post.entity.Content;

public interface ContentRepository extends JpaRepository<Content,Long> {

}
