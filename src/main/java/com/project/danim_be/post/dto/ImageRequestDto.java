package com.project.danim_be.post.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ImageRequestDto {

	private String imageUrl;
	private String imageName;
	private MultipartFile image;

}
