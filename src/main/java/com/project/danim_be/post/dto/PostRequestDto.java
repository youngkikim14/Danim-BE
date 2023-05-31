package com.project.danim_be.post.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	private int groupSize;						//모집인원수
	private String location;					//출발(모집)지역
	private List<String> ageRange;				//연령대
	private List<String> gender;				//성별
	private String keyword;						//키워드
	private List<ContentRequestDto> contents;	//글내용


	@Override
	public String toString() {
		return "Post데이터 로깅" +  '\n' +
			"PostRequestDto{" + '\n' +
			"postTitle='" + postTitle + '\n' +
			", recruitmentStartDate=" + recruitmentStartDate +'\n'+
			", recruitmentEndDate=" + recruitmentEndDate +'\n'+
			", tripStartDate=" + tripStartDate +'\n'+
			", tripEndDate=" + tripEndDate +'\n'+
			", groupSize=" + groupSize +'\n'+
			", location='" + location + '\n'+
			", ageRange='" + ageRange + '\n'+
			", keyword='" + keyword + '\n'+
			", contents=" + contents +
			'}';
	}
}
