package com.project.danim_be.post.dto;

import com.project.danim_be.post.entity.Content;
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
    private Boolean owner;

    public MypagePostResponseDto(Post post, Boolean owner) {
        this.id = post.getId();
        this.title = post.getPostTitle();
        this.tripEndDate = post.getTripEndDate();
        this.imageUrl = null;
        for (Content content : post.getContents()){
            if("image".equals(content.getType())){
                imageUrl=content.getImage().getImageUrl();
                break;
            }
        }
        this.owner = owner;

    }
}
