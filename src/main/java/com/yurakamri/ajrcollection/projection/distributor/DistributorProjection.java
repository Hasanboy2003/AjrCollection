package com.yurakamri.ajrcollection.projection.distributor;

import com.yurakamri.ajrcollection.projection.base.BaseGenericProjection;

public interface DistributorProjection extends BaseGenericProjection<Long> {

    String getName();

    String getDescription();

    Boolean getActive();
}
