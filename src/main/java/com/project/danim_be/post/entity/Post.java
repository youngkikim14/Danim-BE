package com.project.danim_be.post.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.project.danim_be.common.entity.Timestamped;
import com.project.danim_be.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;

@Entity
@Getter
public class Post extends Timestamped {

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
	private String location;	//지역

	@Column(nullable = false)
	private int recruitMember;	//인원수


	@OneToMany(fetch = FetchType.LAZY)
	private List<Content> contents = new ArrayList<>() ;		//내용

	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	@OneToMany
	List<Image> imageList;

}
