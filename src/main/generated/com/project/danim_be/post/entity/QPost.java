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

    public final QContent content;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<Gender> gender = createEnum("gender", Gender.class);

    public final NumberPath<Integer> groupSize = createNumber("groupSize", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Image, QImage> imageUrls = this.<Image, QImage>createList("imageUrls", Image.class, QImage.class, PathInits.DIRECT2);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath keyword = createString("keyword");

    public final EnumPath<Location> location = createEnum("location", Location.class);

    public final QMapApi map;

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
        this.content = inits.isInitialized("content") ? new QContent(forProperty("content"), inits.get("content")) : null;
        this.map = inits.isInitialized("map") ? new QMapApi(forProperty("map"), inits.get("map")) : null;
        this.member = inits.isInitialized("member") ? new com.project.danim_be.member.entity.QMember(forProperty("member")) : null;
    }

}

