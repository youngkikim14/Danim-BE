package com.project.danim_be.post.dto.ResponseDto;

import com.project.danim_be.post.entity.Image;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageResponseDto {

	private Long id;

	private String imageUrl;

	public ImageResponseDto(Image image) {
		this.id =  image.getId();
		this.imageUrl= image.getImageUrl();
	}

}
