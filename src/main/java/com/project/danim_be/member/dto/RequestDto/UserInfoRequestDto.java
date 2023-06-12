package com.project.danim_be.member.dto.RequestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.danim_be.post.entity.Gender;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRequestDto {
    private Long userId;
    @NotBlank
    private Gender gender;
    @NotBlank
    private String ageRange;
    @JsonProperty("AgreeForGender")
    private boolean AgreeForGender;
    @JsonProperty("AgreeForAge")
    private boolean AgreeForAge;
}
