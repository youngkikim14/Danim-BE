package com.project.danim_be.post.dto.RequestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchRequestDto {

    private String ageRange;
    private Integer groupSize;
    private String location;
    private String keyword;
    private String searchKeyword;
    private String gender;
    private Boolean exceptCompletedPost;
}
