package com.project.danim_be.post.repository;

import com.project.danim_be.post.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
