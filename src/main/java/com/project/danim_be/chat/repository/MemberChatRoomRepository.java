package com.project.danim_be.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.danim_be.chat.entity.MemberChatRoom;

public interface MemberChatRoomRepository extends JpaRepository<MemberChatRoom,Long> {
	boolean existsByMember_IdAndChatRoom_RoomId(Long memberId, String roomId);

}
