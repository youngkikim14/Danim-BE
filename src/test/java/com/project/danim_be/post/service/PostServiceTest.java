package com.project.danim_be.post.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.project.danim_be.chat.repository.ChatRoomRepository;
import com.project.danim_be.common.util.Message;
import com.project.danim_be.common.util.S3Uploader;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.member.repository.MemberRepository;
import com.project.danim_be.member.service.MemberService;
import com.project.danim_be.post.dto.RequestDto.PostRequestDto;
import com.project.danim_be.post.entity.Post;
import com.project.danim_be.post.repository.ImageRepository;
import com.project.danim_be.post.repository.PostRepository;

import jakarta.validation.ConstraintViolation;

@SpringBootTest
@Nested
@DisplayName("createPost ")
class PostServiceTest {

	@Mock
	private PostRepository postRepository;
	@Mock
	private ImageRepository imageRepository;
	@Mock
	private ChatRoomRepository chatRoomRepository;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private S3Uploader s3Uploader;
	@Mock
	private MemberService memberService;
	@InjectMocks
	private PostService postService;

	@Nested
	@DisplayName("게시글 작성 성공케이스")
	class postCreateSuccess {

		@Test
		@DisplayName("게시글 작성 성공")
		void createPost() throws ParseException {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			Member member = new Member();

			PostRequestDto postRequestDto = new PostRequestDto();

			postRequestDto.setPostTitle("여름바다고고");                                            //제목
			postRequestDto.setRecruitmentEndDate(dateFormat.parse("2023-07-07"));        //모집시작날짜
			postRequestDto.setRecruitmentStartDate(dateFormat.parse("2023-07-30"));        //모집마감날짜
			postRequestDto.setTripStartDate(dateFormat.parse("2023-08-01"));                //여행시작날짜
			postRequestDto.setTripEndDate(dateFormat.parse("2023-08-15"));                //여행마감날짜
			postRequestDto.setGender("남");                                                    //성별
			postRequestDto.setGroupSize(5);                                                        //모집인원
			postRequestDto.setLocation("서울");                                            //지역
			postRequestDto.setContent("여기는 그냥 스트링값으로 들어갑니다.");                            //내용
			postRequestDto.setKeyword("여기도 그냥 스트링값이 들어갑니다.");                            //키워드
			postRequestDto.setMapAPI("여기도 스트링");                                                //지도좌표

			List<String> ageRange = Arrays.asList("20대", "30대");
			postRequestDto.setAgeRange(ageRange);                                                //연령대
			List<String> imageUrl = Arrays.asList("이미지 url1", "이미지 url2");
			postRequestDto.setContentsImages(imageUrl);                                                //이미지 URL

			ResponseEntity<Message> response = postService.createPost(member, postRequestDto);

			assertNotNull(response);
			assertEquals(HttpStatus.OK, response.getStatusCode());

		}

	}

	@Nested
	@DisplayName("게시글 작성 실패케이스")
	class postCreateFailure {

		@Test
		@DisplayName("날짜형식이 yyyy.MM.dd 으로 들어올때 ")
		void failTest01() {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			Member member = new Member();

			PostRequestDto postRequestDto = new PostRequestDto();

			// ParseException이 발생하는지 확인
			assertThrows(ParseException.class, () -> {
				postRequestDto.setPostTitle("여름바다고고");                                          //제목
				postRequestDto.setRecruitmentEndDate(dateFormat.parse("2023.07.07"));        //모집시작날짜
				postRequestDto.setRecruitmentStartDate(dateFormat.parse("2023-07-30"));      //모집마감날짜
				postRequestDto.setTripStartDate(dateFormat.parse("2023-08-01"));             //여행시작날짜
				postRequestDto.setTripEndDate(dateFormat.parse("2023-08-15"));               //여행마감날짜
				postRequestDto.setGender("남");                                                    //성별
				postRequestDto.setGroupSize(5);                                                        //모집인원
				postRequestDto.setLocation("서울");                                  //지역
				postRequestDto.setContent("여기는 그냥 스트링값으로 들어갑니다.");                         //내용
				postRequestDto.setKeyword("여기도 그냥 스트링값이 들어갑니다.");                           //키워드
				postRequestDto.setMapAPI("여기도 스트링");                                             //지도좌표

				List<String> ageRange = Arrays.asList("20대", "30대");
				postRequestDto.setAgeRange(ageRange);                                                //연령대
				List<String> imageUrl = Arrays.asList("이미지 url1", "이미지 url2");
				postRequestDto.setContentsImages(imageUrl);                                               //이미지 URL
			});
		}

		@Test
		@DisplayName("그룹사이즈 10넘어갈시 ")
		void failTest02() throws Exception {

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			Member member = new Member();

			PostRequestDto postRequestDto = new PostRequestDto();

			postRequestDto.setPostTitle("여름바다고고");                                          //제목
			postRequestDto.setRecruitmentEndDate(dateFormat.parse("2023-07-07"));        //모집시작날짜
			postRequestDto.setRecruitmentStartDate(dateFormat.parse("2023-07-30"));      //모집마감날짜
			postRequestDto.setTripStartDate(dateFormat.parse("2023-08-01"));             //여행시작날짜
			postRequestDto.setTripEndDate(dateFormat.parse("2023-08-15"));               //여행마감날짜
			postRequestDto.setGender("남");                                                    //성별
			postRequestDto.setGroupSize(11);                                                        //모집인원
			postRequestDto.setLocation("서울");                                       //지역
			postRequestDto.setContent("여기는 그냥 스트링값으로 들어갑니다.");                         //내용
			postRequestDto.setKeyword("여기도 그냥 스트링값이 들어갑니다.");                           //키워드
			postRequestDto.setMapAPI("여기도 스트링");                                             //지도좌표

			List<String> ageRange = Arrays.asList("20대", "30대");
			postRequestDto.setAgeRange(ageRange);                                                //연령대
			List<String> imageUrl = Arrays.asList("이미지 url1", "이미지 url2");
			postRequestDto.setContentsImages(imageUrl);


			//validation검사
			LocalValidatorFactoryBean localValidatorFactory = new LocalValidatorFactoryBean();
			localValidatorFactory.afterPropertiesSet();
			//유효성감사를 통과하지못한 필드에대한 정보를  violations에 추가(여기서는 groupSize가추가될거임)
			Set<ConstraintViolation<PostRequestDto>> violations = localValidatorFactory.validate(postRequestDto);

			//isEmpty가아니라서 false가나와서 테스트통과
			assertFalse(violations.isEmpty());

		}

		@Test
		@DisplayName("Enum값 테스트 - 컴파일이되지않아서 실패")
		void failTest03() throws Exception {
		}


		}

	@Nested
	@DisplayName("게시글 수정 성공케이스")
	class postUpdateSuccess{

		@Test
		@DisplayName("게시글수정 성공")
		void updateTest01() throws Exception{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			Member member = new Member("userId", "남", "password", "nickname", "ageRange",  "provider",  true, true, true,
					20D);
			member.setId(1L);

			Post post =new Post(1L, "기존 제목", member);

			when(postRepository.findById(any())).thenReturn(Optional.of(post));
			when(memberRepository.findById(any())).thenReturn(Optional.of(member));

			doReturn(Optional.of(post)).when(postRepository).findById(1L);
			doReturn(Optional.of(member)).when(memberRepository).findById(1L);


			PostRequestDto postRequestDto = new PostRequestDto();

			postRequestDto.setPostTitle("겨울바다고고");
			postRequestDto.setRecruitmentEndDate(dateFormat.parse("2023-07-07"));
			postRequestDto.setRecruitmentStartDate(dateFormat.parse("2023-07-30"));
			postRequestDto.setTripStartDate(dateFormat.parse("2023-08-01"));
			postRequestDto.setTripEndDate(dateFormat.parse("2023-08-15"));
			postRequestDto.setGender("남");
			postRequestDto.setGroupSize(5);
			postRequestDto.setLocation("서울");
			postRequestDto.setContent("여기는 그냥 스트링값으로 들어갑니다.");
			postRequestDto.setKeyword("여기도 그냥 스트링값이 들어갑니다.");
			postRequestDto.setMapAPI("여기도 스트링");

			List<String> ageRange = Arrays.asList("20대", "30대");
			postRequestDto.setAgeRange(ageRange);
			List<String> imageUrl = Arrays.asList("이미지 url1", "이미지 url2");
			postRequestDto.setContentsImages(imageUrl);

			ResponseEntity<Message> response = postService.updatePost(1L, member, postRequestDto );

			assertNotNull(response);
			assertEquals(HttpStatus.OK, response.getStatusCode());

			Optional<Post> updatepost = postRepository.findById(post.getId());
			assertTrue(updatepost.isPresent());
			assertEquals("겨울바다고고", updatepost.get().getPostTitle());

			}

		}

	@Nested
	@DisplayName("게시글 수정 실패케이스")
	class postUpdateFailure{

		@Test
		@DisplayName("수정이 안된케이스")
		void updateTest01() throws Exception{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			Member member = new Member("userId",
				"남",
				"password",
				"nickname",
				"ageRange",
				"provider",
				true,
				true,
				true,
				20D);
			member.setId(1L);

			Post post =new Post(1L, "기존 제목", member);

			when(postRepository.findById(any())).thenReturn(Optional.of(post));
			when(memberRepository.findById(any())).thenReturn(Optional.of(member));

			doReturn(Optional.of(post)).when(postRepository).findById(1L);
			doReturn(Optional.of(member)).when(memberRepository).findById(1L);


			PostRequestDto postRequestDto = new PostRequestDto();

			postRequestDto.setPostTitle("겨울바다고고");
			postRequestDto.setRecruitmentEndDate(dateFormat.parse("2023-07-07"));
			postRequestDto.setRecruitmentStartDate(dateFormat.parse("2023-07-30"));
			postRequestDto.setTripStartDate(dateFormat.parse("2023-08-01"));
			postRequestDto.setTripEndDate(dateFormat.parse("2023-08-15"));
			postRequestDto.setGender("남");
			postRequestDto.setGroupSize(5);
			postRequestDto.setLocation("서울");
			postRequestDto.setContent("여기는 그냥 스트링값으로 들어갑니다.");
			postRequestDto.setKeyword("여기도 그냥 스트링값이 들어갑니다.");
			postRequestDto.setMapAPI("여기도 스트링");

			List<String> ageRange = Arrays.asList("20대", "30대");
			postRequestDto.setAgeRange(ageRange);
			List<String> imageUrl = Arrays.asList("이미지 url1", "이미지 url2");
			postRequestDto.setContentsImages(imageUrl);

			ResponseEntity<Message> response = postService.updatePost(1L, member, postRequestDto );

			assertNotNull(response);
			assertEquals(HttpStatus.OK, response.getStatusCode());

			Optional<Post> updatepost = postRepository.findById(post.getId());
			assertTrue(updatepost.isPresent());
			assertFalse(!postRequestDto.getPostTitle().equals(updatepost.get().getPostTitle()));

		}

	}


}








