package com.yurakamri.ajrcollection.projection;

import com.yurakamri.ajrcollection.projection.base.BaseGenericProjection;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.UUID;

public interface ProductColorProjection extends BaseGenericProjection<UUID> {

    @Value("#{@productRepository.getProductProjectionByProductColorId(target.id)}")
    ProductProjection getProduct();

    @Value("#{@colorRepo.getColorProjectionByProductColorId(target.id)}")
    ColorProjection getColor();

    @Value("#{@attachmentRepo.getAttachmentIdListByProductColorId(target.id)}")
    List<UUID> getAttachments();
}
