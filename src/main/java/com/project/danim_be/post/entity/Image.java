package com.project.danim_be.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String imageUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	private Post post;

	private boolean isDeleted;

	public Image(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void delete() {this.isDeleted = true;}



}
