package com.project.danim_be.common.util;

import com.project.danim_be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RandomNickname {

	private final MemberRepository memberRepository;

	static String[] adjectives = {"세련된", "사랑스러운", "귀여운", "순수한", "깜직한", "앙칼진",
			"단아한", "도도한", "청아한", "우아한", "눈부신", "럭셔리한", "멋진", "상냥한"};
	static String[] nouns = {"호랑이", "사자", "고양이", "강아지", "토끼", "멍뭉이" ,"야옹이", "새벽4시"};

	public String getRandomNickname() {
		Random rand = new Random();
		String nickname = adjectives[rand.nextInt(adjectives.length)] + nouns[rand.nextInt(nouns.length)];

		while (memberRepository.findByNickname(nickname).isPresent()){
			nickname = adjectives[rand.nextInt(adjectives.length)] + nouns[rand.nextInt(nouns.length)];
		}
		return nickname;
	}
}
