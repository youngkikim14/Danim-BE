//package com.project.danim_be.member.controller;
//
//import com.project.danim_be.common.util.Message;
//import com.project.danim_be.member.dto.RequestDto.SignupRequestDto;
//import com.project.danim_be.post.entity.Gender;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.test.context.TestConstructor;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Nested
//@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
//@WebMvcTest(controllers = MemberController.class)
//class MemberControllerTest {
//
//    private MockMvc mvc;
//
//    @Mock
//    private SignupRequestDto signupRequestDto;
//
//    @Test
//    @DisplayName("일반 회원가입 테스트")
//    public void signupTest() throws Exception {
//
//        signupRequestDto = new SignupRequestDto("limslki333@hanmail.net",
//                "1234", "우아한고양이", "30대", Gender.FEMALE, true, true);
//
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/signup")
//                .contentType(MediaType.APPLICATION_JSON);
//
//        MockHttpServletResponse response = mvc.perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse();
//
//    }
//
//}