package com.project.danim_be.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRequestDto {
    private Long userId;
    private String gender;
    private String ageRange;
    @JsonProperty("AgreeForGender")
    private boolean AgreeForGender;
    @JsonProperty("AgreeForAge")
    private boolean AgreeForAge;
}
