package com.project.danim_be.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.danim_be.chat.entity.ChatRoom;
import com.project.danim_be.chat.entity.MemberChatRoom;
import com.project.danim_be.member.entity.Member;

public interface MemberChatRoomRepository extends JpaRepository<MemberChatRoom,Long> {
	boolean existsByMember_IdAndChatRoom_RoomId(Long memberId, String roomId);

	Optional<MemberChatRoom> findByMemberAndChatRoom(Member member, ChatRoom chatRoom);
}
