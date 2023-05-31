package com.project.danim_be.post.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class SearchRequestDto {

    private String ageRange;
    private Boolean typeOfMeeting;
    private String location;
    private String keyword;
    private String searchKeyword;
}
