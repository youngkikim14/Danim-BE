package com.project.danim_be.post.entity;


public enum Gender {

	MALE("남성",1),
	FEMALE("여성",2),
	ALL("모두",3);

	public final String value;
	public final int code;

	Gender(String value, int num) {
		this.value =value;
		this.code = num;
	}
	public String getValue() {return value;}

	public int getCode() {return code;}

	public static Gender fromString(String string) {
		for (Gender gender : Gender.values()) {
			if (gender.name().equalsIgnoreCase(string)) {
				return gender;
			}
		}
		throw new IllegalArgumentException(string + " 값을 찾을수가 없습니다.");
	}


}
