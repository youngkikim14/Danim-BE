package com.project.danim_be.post.dto.ResponseDto;

import com.project.danim_be.post.entity.Gender;
import com.project.danim_be.post.entity.Location;
import com.project.danim_be.post.entity.Post;
import lombok.Getter;

import java.util.Date;

@Getter
public class CardPostResponseDto {

    private Long id;
    private String title;
    private Date recruitmentEndDate;
    private String nickname;
    private int numberOfParticipants;
    private int groupSize;
    private Location location;
    private String keyword;
    private String ageRange;
    private String imageUrl;
    private Gender gender;
    private Boolean isRecruitmentEnd;


    public CardPostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getPostTitle();
        this.recruitmentEndDate = post.getRecruitmentEndDate();
        // if(post.getImageList().size()!=0){
        //     this.imageUrl = post.getImageList().get(0).getImageUrl();
        // }
        this.nickname = post.getMember().getNickname();
        this.numberOfParticipants = post.getNumberOfParticipants();
        this.groupSize = post.getGroupSize();
        this.location = post.getLocation();
        this.keyword = post.getKeyword();
        this.ageRange = String.join(",", post.getAgeRange());
        this.gender = post.getGender();
        this.isRecruitmentEnd = post.getIsRecruitmentEnd();
    }
}
