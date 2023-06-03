package com.project.danim_be.post.dto;

import com.project.danim_be.post.entity.Content;
import com.project.danim_be.post.entity.Post;
import lombok.Getter;

import java.util.Date;

@Getter
public class CardPostResponseDto {

    private Long id;
    private String title;
    private Date tripEndDate;
    private String nickname;
    private int groupSize;
    private String location;
    private String keyword;
    private String ageRange;
    private String imageUrl;


    public CardPostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getPostTitle();
        this.tripEndDate = post.getTripEndDate();
        this.imageUrl = null;

        for (Content content : post.getContents()) {
            if ("image".equals(content.getType())) {
                imageUrl = content.getImage().getImageUrl();
                break;
            }
        }

        this.nickname = post.getMember().getNickname();
        this.groupSize = post.getGroupSize();
        this.location = post.getLocation();
        this.keyword = post.getKeyword();
        this.ageRange = String.join(",", post.getAgeRange());
    }
}
