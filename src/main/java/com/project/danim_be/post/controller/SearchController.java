package com.project.danim_be.post.controller;

import com.project.danim_be.common.util.Message;
import com.project.danim_be.post.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/posts")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/")
    public ResponseEntity<Message> allPosts(Pageable pageable) {
        return searchService.allPosts(pageable);

    }
}
