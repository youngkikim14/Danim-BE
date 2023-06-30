package com.project.danim_be.common.util;

import com.project.danim_be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RandomNickname {

	private final MemberRepository memberRepository;

	static String[] adjectives = {"세련된", "사랑스러운", "귀여운", "순수한", "깜직한", "앙칼진", "단아한", "도도한", "청아한", "우아한",
			"눈부신", "럭셔리한", "멋진", "상냥한", "부엉부엉한", "졸린눈을한", "초롱초롱한", "행복한", "청량한", "부지런한", "소중한"};
	static String[] nouns = {"호랑이", "사자", "고양이", "강아지", "토끼", "멍뭉이", "야옹이", "새벽4시", "부엉이", "두희", "펭귄", "금붕어",
			"개미", "코브라", "사슴", "코끼리", "개구리", "카멜레온", "기러기", "우주인", "청춘"};

	public String getRandomNickname() {

		Random rand = new Random();
		String nickname = adjectives[rand.nextInt(adjectives.length)] + nouns[rand.nextInt(nouns.length)];

		while (memberRepository.findByNickname(nickname).isPresent()){
			nickname = adjectives[rand.nextInt(adjectives.length)] + nouns[rand.nextInt(nouns.length)];
		}

		return nickname;
	}
}
