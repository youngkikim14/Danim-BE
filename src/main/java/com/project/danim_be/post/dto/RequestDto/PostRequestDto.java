package com.project.danim_be.post.dto.RequestDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {

	@Size(min = 0, max = 50 ,message = "최대 50글자까지 입력해주세요")
	private String postTitle;					//게시글제목
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date recruitmentStartDate;				//모집 시작날짜
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date recruitmentEndDate;				//모집 마감날짜
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tripStartDate;					//여행 시작날짜
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tripEndDate;					//여행 마감날짜
	@Max(value = 10)
	private Integer groupSize;					//모집인원수
	private String content;						//글내용
	private String mapAPI;						//지도정보
	private String keyword;
	private String location;					//출발(모집)지역
	private String gender;						//성별

	private List<String> ageRange;					//연령대
	private List<String> contentsImages;				//이미지Url





	
	
}
