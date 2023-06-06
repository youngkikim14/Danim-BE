package com.project.danim_be.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMapApi is a Querydsl query type for MapApi
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMapApi extends EntityPathBase<MapApi> {

    private static final long serialVersionUID = -925244710L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMapApi mapApi = new QMapApi("mapApi");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath map = createString("map");

    public final QPost post;

    public QMapApi(String variable) {
        this(MapApi.class, forVariable(variable), INITS);
    }

    public QMapApi(Path<? extends MapApi> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMapApi(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMapApi(PathMetadata metadata, PathInits inits) {
        this(MapApi.class, metadata, inits);
    }

    public QMapApi(Class<? extends MapApi> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new QPost(forProperty("post"), inits.get("post")) : null;
    }

}

