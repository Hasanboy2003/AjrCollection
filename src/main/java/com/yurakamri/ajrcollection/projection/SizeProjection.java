package com.yurakamri.ajrcollection.projection;

import com.yurakamri.ajrcollection.projection.base.BaseGenericProjection;

public interface SizeProjection extends BaseGenericProjection<Long> {

    String getName();

    Boolean getActive();
}
