package com.yurakamri.ajrcollection.projection;

import com.yurakamri.ajrcollection.projection.base.BaseGenericProjection;

public interface ColorProjection extends BaseGenericProjection<Long> {

    String getName();

    String getCode();
}
