package com.project.danim_be.post.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.danim_be.post.entity.MapApi;

public interface MapApiRepository extends JpaRepository<MapApi,Long> {

	void deleteByPostId(Long id);

	Optional<MapApi> findByPostId(Long id);
}
