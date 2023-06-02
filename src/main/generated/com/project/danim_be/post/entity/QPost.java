package com.project.danim_be.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = -1319294116L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final com.project.danim_be.common.entity.QTimestamped _super = new com.project.danim_be.common.entity.QTimestamped(this);

    public final StringPath ageRange = createString("ageRange");

    public final com.project.danim_be.chat.entity.QChatRoom chatRoom;

    public final ListPath<Content, QContent> contents = this.<Content, QContent>createList("contents", Content.class, QContent.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath gender = createString("gender");

    public final NumberPath<Integer> groupSize = createNumber("groupSize", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath keyword = createString("keyword");

    public final StringPath location = createString("location");

    public final com.project.danim_be.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Integer> numberOfParticipants = createNumber("numberOfParticipants", Integer.class);

    public final StringPath postTitle = createString("postTitle");

    public final DateTimePath<java.util.Date> recruitmentEndDate = createDateTime("recruitmentEndDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> recruitmentStartDate = createDateTime("recruitmentStartDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> tripEndDate = createDateTime("tripEndDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> tripStartDate = createDateTime("tripStartDate", java.util.Date.class);

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new com.project.danim_be.chat.entity.QChatRoom(forProperty("chatRoom"), inits.get("chatRoom")) : null;
        this.member = inits.isInitialized("member") ? new com.project.danim_be.member.entity.QMember(forProperty("member")) : null;
    }

}

