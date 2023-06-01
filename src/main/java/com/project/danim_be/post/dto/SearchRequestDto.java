package com.project.danim_be.post.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class SearchRequestDto {

    private String ageRange;
    private Integer groupSize;
    private String location;
    private String keyword;
    private String searchKeyword;
}
