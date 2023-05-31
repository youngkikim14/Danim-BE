package com.project.danim_be.review.repository;

import com.project.danim_be.post.entity.Post;
import com.project.danim_be.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, QuerydslPredicateExecutor<Review> {

}
