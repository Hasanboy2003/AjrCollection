package com.yurakamri.ajrcollection.projection.brand;

import com.yurakamri.ajrcollection.projection.base.BaseGenericProjection;

import java.util.UUID;

public interface BrandProjection extends BaseGenericProjection<Long> {

    String getName();


    Boolean getActive();

    UUID getLogoId();
}
