package com.project.danim_be.post.entity;

import java.util.Date;

import com.project.danim_be.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String postTitle;	//게시글 제목

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)//날짜 정보만을 저장하기위해 필요한 타입으로설정
	private Date startDate;		//여행 시작날짜

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date endDate;		//여행 종료날짜

	@Column(nullable = false)
	private String ageRange;	//연령대

	@Column(nullable = false)
	private String keyword;		//키워드

	@Column(nullable = false)
	private String content;		//내용

	@Column(nullable = false)
	private String location;	//지역

	@Column(nullable = false)
	private int recruitMember;	//인원수

	private String imageUrl;	//사진

	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;



}
