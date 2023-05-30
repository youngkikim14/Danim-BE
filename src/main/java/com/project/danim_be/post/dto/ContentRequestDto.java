package com.project.danim_be.post.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.danim_be.post.entity.ContentType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentRequestDto {

	private String  type;
	private String level;
	private String text;
	private MultipartFile src;

	@Override
	public String toString() {
		return "Content데이터 로깅" +  '\n' +
			"ContentRequestDto{" + '\n' +
			"type='" + type + '\n' +
			", level=" + level +'\n'+
			", text=" + text +'\n'+
			", imageFile=" + src +'\n'+
			'}';
	}

}
