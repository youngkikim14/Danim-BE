package com.project.danim_be.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Getter
@Setter
@NoArgsConstructor
public class MypageRequestDto {

    private String nickname;
    private String content;
    private MultipartFile image;

}
