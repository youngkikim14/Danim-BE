//package com.project.danim_be.post.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Getter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class MapApi {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	@OneToOne
//	private Post post;
//
//	@Column(columnDefinition = "TEXT")
//	private String map;
//
//	private Boolean isDeleted;
//
//	public void update(String mapAPI) {
//		this.map = mapAPI;
//	}
//	public void delete() {
//		this.isDeleted = true;
//	}
//}
