package com.project.danim_be.post.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.danim_be.post.entity.Content;
import com.project.danim_be.post.entity.ContentType;
import com.project.danim_be.post.entity.Image;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentResponseDto {
	private Long contentId;
	private String  type;
	private String level;
	private String text;

	private Image Image;

	public ContentResponseDto(Content content) {
		this.contentId = content.getId();
		this.type = content.getType();
		this.level = content.getLevel();
		this.text = content.getText();
		this.Image = content.getImage();
	}
}
