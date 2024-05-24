package com.yurakamri.ajrcollection.projection;

import com.yurakamri.ajrcollection.projection.base.BaseGenericProjection;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.UUID;

public interface IncomeDetailProjection extends BaseGenericProjection<UUID> {

    Integer getQuantity();

    BigDecimal getIncomePrice();

    @Value("#{@sizeRepository.getSizeProjectionByIncomeDetailId(target.id)}")
    SizeProjection getSize();

    @Value("#{@productColorRepository.getProductColorProjectionByIncomeDetailId(target.id)}")
    ProductColorProjection getProductColor();
}
