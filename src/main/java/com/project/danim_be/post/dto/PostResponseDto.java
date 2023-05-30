package com.project.danim_be.post.dto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.project.danim_be.post.entity.Post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDto {

	private Long 		postId;			//게시글번호
	private String 		postTitle;		//게시글제목
	private Date 		startDate;		//여행 시작날짜
	private Date  		endDate;		//여행 종료날짜
	private String 		ageRange;		//연령대
	private String 		keyword;		//키워드
	private int 		groupSize;	//인원수
	private String 		location;		//지역

	private List<ContentResponseDto> contents;		//게시글


	public PostResponseDto(Post post){
		this.postId = post.getId();
		this.postTitle = post.getPostTitle();
		this.startDate = post.getStartDate();
		this.endDate = post.getEndDate();
		this.ageRange = post.getAgeRange();
		this.keyword = post.getKeyword();
		this.groupSize = post.getGroupSize();
		this.location = post.getLocation();
		this.contents = post.getContents().stream()
			.map(ContentResponseDto::new)
			.collect(Collectors.toList());


	}

}
