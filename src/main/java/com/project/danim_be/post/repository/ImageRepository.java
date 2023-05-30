package com.project.danim_be.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.danim_be.post.entity.Image;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
