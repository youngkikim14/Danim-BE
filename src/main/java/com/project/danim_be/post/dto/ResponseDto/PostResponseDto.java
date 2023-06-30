package com.project.danim_be.post.dto.ResponseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.danim_be.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostResponseDto implements Serializable {

	private Long 		  id;

	private String		nickName;

	private String 		postTitle;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate recruitmentStartDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate recruitmentEndDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate tripStartDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate tripEndDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime modifiedAt;

	private Long chatRoomId;

	private String map;

	private int groupSize;

	private String keyword;

	private String location;

	private List<String> ageRange;

	private List<String> gender;

	private Integer numberOfParticipants;

	private String myPageImageUrl;

	private List<Long> participants;

	private String content;

	private Boolean isComplete;

	public PostResponseDto(Post post){
		this.id = post.getId();
		this.nickName = post.getMember().getNickname();
		this.myPageImageUrl = post.getMember().getImageUrl();
		this.postTitle = post.getPostTitle();
		this.recruitmentStartDate=post.getRecruitmentStartDate();
		this.recruitmentEndDate=post.getRecruitmentEndDate();
		this.tripStartDate = post.getTripStartDate();
		this.tripEndDate = post.getTripEndDate();
		this.location = post.getLocation();
		this.groupSize = post.getGroupSize();
		this.keyword = post.getKeyword();
		this.ageRange = post.getAgeRange();
		this.gender = post.getGender();
		this.content = post.getContent();
		this.map = post.getMap();
		this.chatRoomId = post.getChatRoom().getId();
		this.createdAt = post.getCreatedAt();
		this.modifiedAt = post.getModifiedAt();
		this.numberOfParticipants= post.getNumberOfParticipants();
		this.isComplete = post.getGroupSize().equals(post.getNumberOfParticipants());
	}

	public PostResponseDto(Post post, List<Long> participants){
		this.id = post.getId();
		this.nickName = post.getMember().getNickname();
		this.myPageImageUrl = post.getMember().getImageUrl();
		this.postTitle = post.getPostTitle();
		this.recruitmentStartDate=post.getRecruitmentStartDate();
		this.recruitmentEndDate=post.getRecruitmentEndDate();
		this.tripStartDate = post.getTripStartDate();
		this.tripEndDate = post.getTripEndDate();
		this.location = post.getLocation();
		this.groupSize = post.getGroupSize();
		this.keyword = post.getKeyword();
		this.ageRange = post.getAgeRange();
		this.gender = post.getGender();
		this.content = post.getContent();
		this.map = post.getMap();
		this.chatRoomId = post.getChatRoom().getId();
		this.createdAt = post.getCreatedAt();
		this.modifiedAt = post.getModifiedAt();
		this.numberOfParticipants= post.getNumberOfParticipants();
		this.participants = participants;
		this.isComplete = post.getGroupSize().equals(post.getNumberOfParticipants());
	}

}
