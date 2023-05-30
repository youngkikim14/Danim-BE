package com.project.danim_be.post.dto;

import com.project.danim_be.post.entity.Post;
import lombok.Getter;

import java.util.Date;

@Getter
public class AllPostResponseDto {

    private Long id;
    private String title;
    private Date endDate;
    private String imageUrl;

    public AllPostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getPostTitle();
        this.endDate = post.getEndDate();
        this.imageUrl = post.getImageList().get(0).getImageUrl();
    }
}
