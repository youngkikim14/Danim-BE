package com.project.danim_be.member.service;

import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonParser;
import com.project.danim_be.common.util.Message;
import com.project.danim_be.common.util.RandomNickname;
import com.project.danim_be.common.util.StatusEnum;
import com.project.danim_be.member.dto.MemberRequestDto;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.member.repository.MemberRepository;
import com.project.danim_be.security.refreshToken.RefreshTokenRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NaverService {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String tokenUrl;
    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String userInfoUrl;

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseEntity<Message> naverLogin(String code, String state, HttpServletResponse response) throws IOException {

        String accessToken = getAccessToken(code, state);
        System.out.println(accessToken);

        MemberRequestDto memberRequestDto = getNaverUserInfo(accessToken);

        Member member = saveMember(memberRequestDto);

//        forceLogin(member);

//        createToken(member, response);

        return ResponseEntity.ok(Message.setSuccess(StatusEnum.OK, "회원가입 성공"));
    }

    private String getAccessToken(String code, String state) throws IOException {

        URL url = new URL(tokenUrl);
        // url로 연결
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션
        connection.setDoOutput(true);

        // params 전송
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        String params = "grant_type=authorization_code" +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&redirect_uri= http://localhost:8080/api/user/naver/callback" +
                "&code=" + code +
                "&state=" + state;
        bw.write(params);
        bw.flush();
        bw.close();

        // 응답 데이터 줄별로 읽어오기
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String oneLine;
        StringBuilder result = new StringBuilder();

        while ((oneLine = br.readLine()) != null) {
            result.append(oneLine);
        }
        br.close();

        // JSON 파싱해서 access_token만 리턴
        JsonElement tokenData = JsonParser.parseString(result.toString());
        return tokenData.getAsJsonObject().get("access_token").getAsString();

//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code");
//        params.add("client_id", clientId);
//        params.add("client_secret", clientSecret);
//        params.add("code", code);
//        params.add("state", state);
//
//        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(params, headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> naverResponse = rt.exchange(
//                tokenUrl,
//                HttpMethod.POST,
//                naverTokenRequest,
//                String.class
//        );
//
//        String responseBody = naverResponse.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//        return jsonNode.get("access_token").asText();
    }

    // 네이버에 요청해서 회원정보 받는 메소드
    public MemberRequestDto getNaverUserInfo(String accessToken) throws IOException {

        URL url = new URL(userInfoUrl);
        // url로 연결
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션
        connection.setDoOutput(true);

        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        // 응답 데이터 줄별로 읽어오기
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String oneLine;
        StringBuilder result = new StringBuilder();

        while ((oneLine = br.readLine()) != null) {
            result.append(oneLine);
        }
        br.close();

        // JSON 파싱해서 access_token만 리턴
        JsonElement userInfoData = JsonParser.parseString(result.toString());
        String email = String.valueOf(userInfoData.getAsJsonObject().get("response").getAsJsonObject().get("email"));
        String age = String.valueOf(userInfoData.getAsJsonObject().get("response").getAsJsonObject().get("age"));
        String gender = String.valueOf(userInfoData.getAsJsonObject().get("response").getAsJsonObject().get("gender"));

//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        // HTTP 요청 보내기
//        HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.exchange(
//                userInfoUrl,
//                HttpMethod.POST,
//                naverUserInfoRequest,
//                String.class
//        );
//
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper2 = new ObjectMapper();
//        JsonNode jsonNode2 = objectMapper2.readTree(responseBody);
//
////        Long id = jsonNode2.get("response").get("id").asLong();
//        String email = jsonNode2.get("response").get("email").asText();
//        String age = jsonNode2.get("response").get("age").asText();
//        String gender = jsonNode2.get("response").get("gender").asText();
//
        return new MemberRequestDto(email, age, gender);
    }

    private Member saveMember(MemberRequestDto memberRequestDto) {

        Member naverMember = memberRepository.findByUserId(memberRequestDto.getUserId()).orElse(null);
        String nickname = RandomNickname.getRandomNickname();
        if(naverMember == null) {
            Member member = Member.builder()
                    .email(memberRequestDto.getUserId())
                    .ageRange(memberRequestDto.getAgeRange())
                    .gender(memberRequestDto.getGender())
                    .nickname(nickname)
                    .provider("NAVER")
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .build();

            System.out.println(memberRequestDto.getUserId());
            System.out.println(memberRequestDto.getAgeRange());
            System.out.println(memberRequestDto.getGender());
            System.out.println(passwordEncoder.encode(UUID.randomUUID().toString()));
            memberRepository.save(member);
            return member;
        }
        return naverMember;
    }

//    private void forceLogin(Member member) {
//        UserDetails userDetails = new UserDetailsImpl(member);
//        if (member.getIsDeleted().equals(true)) {
//            throw new CustomException(ErrorCode.DELETED_USER_EXCEPTION);
//        }
//        Authentication authentication =
//                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }

//    private void createToken(Member member, HttpServletResponse response) {
//        TokenDto tokenDto = jwtUtil.createAllToken(member.getEmail());
//
//        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(member.getEmail());
//
//        if (refreshToken.isPresent()) {
//            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
//        } else {
//            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), member.getEmail());
//            refreshTokenRepository.save(newToken);
//        }
//
//        setHeader(response, tokenDto);
//    }
}
