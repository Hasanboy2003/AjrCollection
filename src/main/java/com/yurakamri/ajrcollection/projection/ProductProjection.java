package com.yurakamri.ajrcollection.projection;

import com.yurakamri.ajrcollection.projection.base.BaseGenericProjection;
import com.yurakamri.ajrcollection.projection.brand.BrandProjection;
import com.yurakamri.ajrcollection.projection.category.CategoryProjection;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductProjection extends BaseGenericProjection<UUID> {

    String getName();

    String getDescription();

    Boolean getActive();

    BigDecimal getOutcomePrice();

    String getCode();

    Long getDiscountId();

    Boolean getIsOnDiscount();

    BigDecimal getDiscountPercent();

    BigDecimal getDiscountPrice();

    @Value("#{@brandRepository.getBrandProjectionByProductId(target.id)}")
    BrandProjection getBrand();

    @Value("#{@categoryRepository.getCategoryProjectionByProductId(target.id)}")
    CategoryProjection getCategory();
}
