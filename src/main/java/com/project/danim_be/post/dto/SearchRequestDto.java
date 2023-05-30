package com.project.danim_be.post.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class SearchRequestDto {

    private List<String> ageRange;
    private Boolean typeOfMeeting;
    private String location;
    private List<String> keyword;
    private String searchKeyword;
}
