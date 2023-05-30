package com.project.danim_be.post.controller;

import com.project.danim_be.common.util.Message;
import com.project.danim_be.post.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/search")
    public ResponseEntity<Message> searchPosts(Pageable pageable,
            @RequestParam(value = "ageRange", required = false) String ageRange,
            @RequestParam(value = "typeOfMeeting", required = false) String typeOfMeeting,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword){
        return searchService.searchPost(pageable, ageRange, typeOfMeeting, location, keyword, searchKeyword);
    }
}
