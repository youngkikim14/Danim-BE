package com.project.danim_be.post.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter

public class ImageRequestDto {

	private String imageUrl;
	private String imageName;
	private MultipartFile image;

}
