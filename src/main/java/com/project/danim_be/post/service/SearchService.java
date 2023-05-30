package com.project.danim_be.post.service;

import com.project.danim_be.common.util.Message;
import com.project.danim_be.common.util.StatusEnum;
import com.project.danim_be.post.dto.AllPostResponseDto;
import com.project.danim_be.post.entity.Post;
import com.project.danim_be.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final PostRepository postRepository;

    //전체 조회
    public ResponseEntity<Message> allPosts(Pageable pageable){
        List<Post> postList = postRepository.findAllByOrderByCreatedAt(pageable);
        List<AllPostResponseDto> allPostResponseDtoList = new ArrayList<>();
        for (Post post : postList) {
            allPostResponseDtoList.add(new AllPostResponseDto(post));
        }
        return ResponseEntity.ok(Message.setSuccess(StatusEnum.OK, "전체 데이터 조회성공", allPostResponseDtoList));
    }
}
