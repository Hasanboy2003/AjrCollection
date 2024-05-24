package com.yurakamri.ajrcollection.projection.category;

import com.yurakamri.ajrcollection.projection.base.BaseGenericProjection;

import java.util.UUID;

public interface CategoryProjection extends BaseGenericProjection<UUID> {

    String getName();

    String getDescription();

    Boolean getActive();

//    @Value("#{@categoryRepository.getCategoryProjectionById(target.parentCategoryId)}")
//    CategoryProjection getParentCategory();
    // todo: implement logic for parent category as well
}
