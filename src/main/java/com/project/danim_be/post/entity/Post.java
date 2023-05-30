package com.project.danim_be.post.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.project.danim_be.common.entity.Timestamped;
import com.project.danim_be.member.entity.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String postTitle;	//게시글 제목

	@Column(nullable = false)
	private Date startDate;		//여행 시작날짜

	@Column(nullable = false)
	private Date  endDate;		//여행 종료날짜

	@Column(nullable = false)
	private String ageRange;	//연령대

	@Column(nullable = false)
	private String keyword;		//키워드

	// @Column(nullable = false)
	private String location;	//지역

	@Column(nullable = false)
	private int groupSize;	//인원수

	@Column(nullable = false)
	private Boolean typeOfMeeting; // 1명이면 true, 2명부터 false

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	@Builder.Default
	private List<Content> contents =  new ArrayList<>();		//내용

	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	public void setTypeOfMeeting(Boolean typeOfMeeting) {
		this.typeOfMeeting = typeOfMeeting;
	}
}
