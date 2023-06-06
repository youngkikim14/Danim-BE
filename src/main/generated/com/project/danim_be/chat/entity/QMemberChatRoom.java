package com.project.danim_be.chat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberChatRoom is a Querydsl query type for MemberChatRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberChatRoom extends EntityPathBase<MemberChatRoom> {

    private static final long serialVersionUID = 64997761L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberChatRoom memberChatRoom = new QMemberChatRoom("memberChatRoom");

    public final QChatRoom chatRoom;

    public final DateTimePath<java.time.LocalDateTime> firstJoinRoom = createDateTime("firstJoinRoom", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath kickMember = createBoolean("kickMember");

    public final com.project.danim_be.member.entity.QMember member;

    public final DateTimePath<java.time.LocalDateTime> recentConnect = createDateTime("recentConnect", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> recentDisConnect = createDateTime("recentDisConnect", java.time.LocalDateTime.class);

    public QMemberChatRoom(String variable) {
        this(MemberChatRoom.class, forVariable(variable), INITS);
    }

    public QMemberChatRoom(Path<? extends MemberChatRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberChatRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberChatRoom(PathMetadata metadata, PathInits inits) {
        this(MemberChatRoom.class, metadata, inits);
    }

    public QMemberChatRoom(Class<? extends MemberChatRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new QChatRoom(forProperty("chatRoom"), inits.get("chatRoom")) : null;
        this.member = inits.isInitialized("member") ? new com.project.danim_be.member.entity.QMember(forProperty("member")) : null;
    }

}

