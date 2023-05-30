package com.project.danim_be.post.entity;

import lombok.NoArgsConstructor;

@NoArgsConstructor

public enum ContentType {

	HEADING("헤더"),
	IMAGE("이미지"),
	PARAGRAPH("문단");

	private String value;

	ContentType(String value){
		this.value = value;
	}


}
