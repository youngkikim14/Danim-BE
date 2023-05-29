package com.project.danim_be.post.dto;

import com.project.danim_be.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class MypagePostResponseDto {

    private Long id;
    private String title;
    private Date endDate;

    public MypagePostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getPostTitle();
        this.endDate = post.getEndDate();
    }
}
