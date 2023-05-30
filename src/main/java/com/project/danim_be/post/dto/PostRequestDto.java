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

	private String postTitle;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	private int recruitMember;
	private String location;
	private String ageRange;
	private String keyword;
	private List<ContentRequestDto> contents;

	// @Override
	// public String toString() {
	// 	return "Post데이터 로깅" +  '\n' +
	// 		"PostRequestDto{" + '\n' +
	// 		"postTitle='" + postTitle + '\n' +
	// 		", startDate=" + startDate +'\n'+
	// 		", endDate=" + endDate +'\n'+
	// 		", recruitMember=" + recruitMember +'\n'+
	// 		", location='" + location + '\n'+
	// 		", ageRange='" + ageRange + '\n'+
	// 		", keyword='" + keyword + '\n'+
	// 		", contents=" + contents +
	// 		'}';
	// }
}
