package com.project.danim_be.post.dto;

import com.project.danim_be.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class MypagePostResponseDto {

    private Long id;
    private String title;
    private Date tripEndDate;
    private String imageUrl;

    public MypagePostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getPostTitle();
        this.tripEndDate = post.getTripEndDate();
        this.imageUrl = post.getContents().get(0).getImage().getImageUrl();
    }
}
