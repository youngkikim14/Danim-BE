package com.project.danim_be.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.danim_be.common.util.Message;
import com.project.danim_be.common.util.StatusEnum;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleService {

    private final Environment env;
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<Message> socialLogin(String code) { // 컨트롤러에서 registrationId 값이 들어왔다면 각 메서드에 대입하여 그에 맞는 소셜 로그인 구현
        String accessToken = getAccessToken(code);
        JsonNode userResourceNode = getUserResource(accessToken);
//        System.out.println("userResourceNode = " + userResourceNode);

        String id = userResourceNode.get("id").asText();
        String email = userResourceNode.get("email").asText();
        String googleNickname = userResourceNode.get("name").asText();
//        System.out.println("id = " + id);
//        System.out.println("email = " + email);
//        System.out.println("nickname = " + googleNickname);
        if(memberRepository.findByUserId(email).isEmpty()){
            String password = UUID.randomUUID().toString();
            String nickname = UUID.randomUUID() + googleNickname;
            Member member = new Member(email, password, nickname);
            memberRepository.saveAndFlush(member);
        }
        return ResponseEntity.ok(Message.setSuccess(StatusEnum.OK, "회원가입 성공"));
    }

    private String getAccessToken(String authorizationCode) {
        String clientId = env.getProperty("oauth2." + "google." + ".client-id");
        String clientSecret = env.getProperty("oauth2." + "google." + ".client-secret");
        String redirectUri = env.getProperty("oauth2." + "google." + ".redirect-uri");
        String tokenUri = env.getProperty("oauth2." + "google." + ".token-uri");

        // multivaluemap 으로 params에 각 값들을 담아둠. 그냥 map을 한번에 처리할 수 있는 형태 별거 없음.
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        //httpheader 객체를 만들고 거기에 헤더를 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 헤더와 params값을 http 바디에 담음
        HttpEntity entity = new HttpEntity(params, headers);

        // resttemplate를 이용해서 구글의 토근 uri에, post형식으로, httpentity에 담긴 값들을, JsonNode 형식으로 응답받음
        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);

        // jsonnode로 json 형태의 값의 토큰 값을 가져옴
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }

    private JsonNode getUserResource(String accessToken) {
        // 프로퍼티스에 있는 구글의 유저정보 서버
        String resourceUri = env.getProperty("oauth2."+"google."+".resource-uri");

        // http 헤더 객체 만들고
        HttpHeaders headers = new HttpHeaders();

        //헤더에 토큰 박아줌
        headers.set("Authorization", "Bearer " + accessToken);

        //http에 헤더 담음
        HttpEntity entity = new HttpEntity(headers);

        // restTemplate로 유저정보 서버에 가서, get 방식으로 엔티티 가져다가 JasonNode 형식으로 json 데이터로 가져옴
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }
}