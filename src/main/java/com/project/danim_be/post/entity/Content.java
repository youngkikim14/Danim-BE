package com.project.danim_be.post.entity;

import java.util.List;

import com.project.danim_be.post.dto.ContentRequestDto;
import com.project.danim_be.post.dto.PostRequestDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Content {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String  type;

	private String level;

	private String text;

	private Boolean isDeleted;


	@OneToOne(mappedBy = "content",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Image image;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	public void delete() {
		this.isDeleted = true;

		if(this.image!=null){
			this.image.delete();
		}


	}




}
