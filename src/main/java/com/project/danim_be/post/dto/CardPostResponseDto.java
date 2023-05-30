package com.project.danim_be.post.dto;

import com.project.danim_be.post.entity.Post;
import lombok.Getter;

import java.util.Date;

@Getter
public class CardPostResponseDto {

    private Long id;
    private String title;
    private Date endDate;
    private String imageUrl;
    private String nickname;
    private int groupSize;
    private String location;
    private String keyword;
    private String ageRange;

    public CardPostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getPostTitle();
        this.endDate = post.getEndDate();
        this.imageUrl = post.getContents().get(0).getImageLists().get(0).getImageUrl();
        this.nickname = post.getMember().getNickname();
        this.groupSize = post.getGroupSize();
        this.location = post.getLocation();
        this.keyword = post.getKeyword();
        this.ageRange = post.getAgeRange();
    }
}
