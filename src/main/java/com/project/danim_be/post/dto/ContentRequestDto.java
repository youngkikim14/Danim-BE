package com.project.danim_be.post.dto;

import java.util.List;

import com.project.danim_be.post.entity.ContentType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentRequestDto {

	private ContentType type;
	private String level;
	private String text;
	private String alt;
	private List<ImageRequestDto> imageLists;

}
