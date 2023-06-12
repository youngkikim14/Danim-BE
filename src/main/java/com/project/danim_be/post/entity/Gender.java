package com.project.danim_be.post.entity;


public enum Gender {

	MALE("남성"),
	FEMALE("여성"),
	ALL("모두");

	public final String value;

	Gender(String value) {
		this.value =value;
	}
	public String getValue() {return value;}


	public static Gender fromString(String string) {
		for (Gender gender : Gender.values()) {
			if (gender.name().equalsIgnoreCase(string)) {
				return gender;
			}
		}
		throw new IllegalArgumentException(string + " 값을 찾을수가 없습니다.");
	}


}
