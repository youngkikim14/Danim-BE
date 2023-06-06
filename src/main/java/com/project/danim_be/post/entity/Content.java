package com.project.danim_be.post.entity;

import java.util.List;

import com.project.danim_be.post.dto.ContentRequestDto;
import com.project.danim_be.post.dto.PostRequestDto;

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
public class Content {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String content;
	private Boolean isDeleted;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	public void delete() {
		this.isDeleted = true;
	}

	public Content(PostRequestDto requestDto){

		this.content = requestDto.getContent();
	}

	public void update(String content) {
		this.content = content;
	}
}
