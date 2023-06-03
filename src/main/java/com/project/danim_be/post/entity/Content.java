package com.project.danim_be.post.entity;

import jakarta.persistence.*;
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
