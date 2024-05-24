package com.yurakamri.ajrcollection.projection;

import com.yurakamri.ajrcollection.projection.base.BaseGenericProjection;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.List;

public interface DiscountProjection extends BaseGenericProjection<Long> {

    String getStartDate();

    String getEndDate();

    BigDecimal getPercent();

    @Value("#{@productRepository.getProductsProjectionByDiscountId(target.id)}")
    List<ProductProjection> getProducts();
}
