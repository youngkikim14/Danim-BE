package com.project.danim_be.post.dto.ResponseDto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// @JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentResponseDto {
	private Long contentId;
	private String  type;
	private String level;
	private String text;

	private ImageResponseDto image;


	// public ContentResponseDto(Content content) {
	// 	this.contentId = content.getId();
	// 	this.type = content.getType();
	// 	this.level = content.getLevel();
	// 	this.text = content.getText();
	//
	// }
}
