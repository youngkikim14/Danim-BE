package com.project.danim_be.post.entity;


public enum Location {
	SEOUL("서울",1),
	GYEONGGI("경기도",2),
	INCHEON("인천",3),
	BUSAN("부산",4),
	DAEGU("대구",5),
	DAEJEON("대전",6),
	ULSAN("울산",7),
	GWANGJU("광주",8),
	JEJU("제주",9),
	GANGWON("강원도",10),;

	public final String value;
	public final int code;

	Location(String value, int num) {
		this.value =value;
		this.code = num;
	}
	public String getValue() {return value;}

	public int getCode() {return code;}

	public static Location fromString(String string) {
		for (Location location : Location.values()) {
			if (location.name().equalsIgnoreCase(string)) {
				return location;
			}
		}
		throw new IllegalArgumentException(string + " 값을 찾을수가 없습니다.");
	}



}