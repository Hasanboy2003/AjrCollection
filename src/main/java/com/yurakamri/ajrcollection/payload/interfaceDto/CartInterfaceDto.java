package com.yurakamri.ajrcollection.payload.interfaceDto;

import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

public interface CartInterfaceDto {
    UUID getId();

    @Value("#{@userRepo.findByCartItemId(target.id)}")
    InterfaceUserDto getUserId();

    @Value("#{@colorRepo.findByCartItemId(target.id)}")
    String getColorCode();

    @Value("#{@productRepository.findByCartItemId(target.id)}")
    ProductInterfaceDto getProduct();

    @Value("#{@sizeRepository.findByCartItemDto(target.id)}")
    String getSizeName();

    Integer getQuantity();

}
