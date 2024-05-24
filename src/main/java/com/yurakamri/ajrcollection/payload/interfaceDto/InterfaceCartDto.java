package com.yurakamri.ajrcollection.payload.interfaceDto;


import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.UUID;

public interface InterfaceCartDto {
    UUID getId();

    @Value("#{@colorRepo.findByCartItemId(target.id)}")
    String getColorCode();

    @Value("#{@productRepository.findByCartItemId(target.id)}")
    InterfaceProductDto getProduct();

    @Value("#{@sizeRepository.findByCartItemDto(target.id)}")
    String getSizeName();

    Integer getQuantity();

    BigDecimal getFixedPrice();
}
