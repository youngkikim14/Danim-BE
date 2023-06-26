package com.project.danim_be.post.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.project.danim_be.chat.entity.ChatRoom;
import com.project.danim_be.common.entity.Timestamped;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.post.dto.RequestDto.PostRequestDto;
import jakarta.persistence.*;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Post extends Timestamped implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Name("postId")
	private Long id;

	@Column(nullable = false)
	private String postTitle;            		//게시글 제목

	@Column(nullable = false)
	private Date recruitmentStartDate;    		//모집 등록날짜

	@Column(nullable = false)
	private Date recruitmentEndDate;    		//모집 마감날짜

	@Column(nullable = false)
	private Date tripStartDate;            		//여행 시작날짜

	@Column(nullable = false)
	private Date tripEndDate;            		//여행 종료날짜

	@Column(nullable = false)
	private String ageRange;            		//연령대

	@Column(nullable = false)
	private String gender;						//성별

	@Column(nullable = false)
	private String keyword;                		//키워드

	@Column(nullable = false)
	private String location;            		//지역

	@Column(nullable = false)
	private Integer groupSize;            		//인원수

	private Boolean isRecruitmentEnd;			//모집 마감기한 지났는지 여부

	@Column(nullable = false)
	private Integer numberOfParticipants ;    	//현재참여자수

	@Column(columnDefinition = "TEXT")
	private String content;						// 내용

	@Column(columnDefinition = "TEXT")
	private String map;							// 지도 좌표

	@OneToOne(mappedBy = "post",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private ChatRoom chatRoom;

	@OneToMany(mappedBy = "post",cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
	private List<Image> imageUrls;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberId")
	private Member member;

	private Boolean isDeleted;

	//테스트용생성자
	public Post(long l, String s, Member member) {
		this.id = l;
		this.postTitle = s;
		this.member = member;
	}

	//연령대 복수선택가능
	public void setAgeRange(List<String> ageRange) {
		this.ageRange = String.join(",", ageRange);
	}

	public List<String> getAgeRange() {
		return new ArrayList<>(Arrays.asList(this.ageRange.split(",")));
	}

	//성별 복수선택 가능
	public void setGender(List<String> gender) {
		this.gender = String.join(",", gender);
	}

	public List<String> getGender() {
		return new ArrayList<>(Arrays.asList(this.gender.split(",")));
	}

	public void update(PostRequestDto requestDto) {
		this.postTitle = requestDto.getPostTitle();
		this.recruitmentStartDate = requestDto.getRecruitmentStartDate();
		this.recruitmentEndDate = requestDto.getRecruitmentEndDate();
		this.tripStartDate = requestDto.getTripStartDate();
		this.tripEndDate = requestDto.getTripEndDate();
		this.groupSize = requestDto.getGroupSize();
		this.location = requestDto.getLocation();
		this.keyword = requestDto.getKeyword();
		this.gender = requestDto.getGender();
		this.setAgeRange(requestDto.getAgeRange());
		this.content = requestDto.getContent();
		this.map = requestDto.getMapAPI();
	}

	public void delete() {
		this.isDeleted = true;
	}

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}

	public void incNumberOfParticipants() {
		++numberOfParticipants;
	}

    public void decNumberOfParticipants() {
		--numberOfParticipants;
    }

	public void endRecruitmentDate() {
		this.isRecruitmentEnd = true;
	}

}
