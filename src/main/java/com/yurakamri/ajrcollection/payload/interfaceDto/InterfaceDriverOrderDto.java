package com.yurakamri.ajrcollection.payload.interfaceDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface InterfaceDriverOrderDto {
    UUID getId();
    LocalDate getCreatedAt();
    BigDecimal getShippingAmount();
    String getCode();
    String getOrderStatusName();

}
