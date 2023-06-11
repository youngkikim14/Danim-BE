package com.project.danim_be.post.dto;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

import com.project.danim_be.post.entity.Gender;
import com.project.danim_be.post.entity.Location;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {

	private String postTitle;					//게시글제목
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date recruitmentStartDate;			//모집 시작날짜
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date recruitmentEndDate;			//모집 마감날짜
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tripStartDate;					//여행 시작날짜
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tripEndDate;					//여행 마감날짜
	@Max(value = 10)
	private Integer groupSize;					//모집인원수
	private String content;						//글내용
	private String mapAPI;						//지도정보

	private String keyword;
	private Location location;					//출발(모집)지역
	private Gender gender;						//성별

	private List<String> ageRange;				//연령대
	private List<String> imageUrls;				//이미지Url





	
	
}
