package com.yurakamri.ajrcollection.payload.interfaceDto;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface OrderProjection {
    UUID getId();

    LocalDate getCreatedAt();

    String getLongitude();

    String getLatitude();

    BigDecimal getShippingAmount();

    String getCode();

    String getAddress();

    String getPhoneNumber();

    String getDriverPhoneNumber();

    BigDecimal getMyCart();

    BigDecimal getCharityCart();

    BigDecimal getTotalMoney();

    @Value("#{@orderStatusRepo.findByOrderId(target.id)}")
    String getOrderStatusName();

    @Value("#{@cartRepo.findAllByOrderId(target.id)}")
    List<InterfaceCartDto> getCartItems();
}
