package com.project.danim_be.post.dto.RequestDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {

	@Size(min = 0, max = 50, message = "최대 50글자까지 입력해주세요")
	private String postTitle;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date recruitmentStartDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date recruitmentEndDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tripStartDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tripEndDate;

	@Max(value = 10)
	private Integer groupSize;

	private String content;

	private String mapAPI;

	private String keyword;

	private String location;

	private String gender;

	private List<String> ageRange;

	private List<String> contentsImages;
	
}
