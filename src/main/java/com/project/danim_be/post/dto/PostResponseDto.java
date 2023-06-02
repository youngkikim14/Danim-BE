package com.project.danim_be.post.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.danim_be.chat.entity.ChatRoom;
import com.project.danim_be.post.entity.Content;
import com.project.danim_be.post.entity.Post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDto {


	private Long 		postId;					//게시글번호
	private String 		postTitle;				//게시글제목
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date recruitmentStartDate;			//모집 시작날짜
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date recruitmentEndDate;			//모집 마감날짜
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date tripStartDate;					//여행 시작날짜
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date 		tripEndDate;			//여행 마감날짜
	private List<String> ageRange;				//연령대
	private List<String> gender;				//연령대
	private String 		 keyword;				//키워드
	private int 		 groupSize;				//인원수
	private String 		 location;				//지역
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private List<ContentResponseDto> contents;		//게시글



	public PostResponseDto(Post post){
		this.postId = post.getId();
		this.postTitle = post.getPostTitle();
		this.recruitmentStartDate=post.getRecruitmentStartDate();
		this.recruitmentEndDate=post.getRecruitmentEndDate();
		this.tripStartDate = post.getTripStartDate();
		this.tripEndDate = post.getTripEndDate();
		this.ageRange = post.getAgeRange();
		this.keyword = post.getKeyword();
		this.groupSize = post.getGroupSize();
		this.location = post.getLocation();
		List<ContentResponseDto> contentResponseDtoList = new ArrayList<>();
		for (Content content : post.getContents()) {
			contentResponseDtoList.add(new ContentResponseDto(content));
		}
		this.contents = contentResponseDtoList;
		this.createdAt = post.getCreatedAt();
		this.modifiedAt = post.getModifiedAt();
	}
}
