package com.yurakamri.ajrcollection.projection.base;

public interface BaseGenericProjection<T> {

    T getId();

    String getCreatedAt();

    String getUpdatedAt();
}
