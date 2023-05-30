package com.project.danim_be.post.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentRequestDto {

	private String type;
	private String level;
	private String text;
	private String alt;
	private List<ImageRequestDto> imageLists;

}
