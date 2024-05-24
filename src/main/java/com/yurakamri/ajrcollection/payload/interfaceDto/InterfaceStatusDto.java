package com.yurakamri.ajrcollection.payload.interfaceDto;

import com.yurakamri.ajrcollection.entity.enums.OrderStatusEnum;

public interface InterfaceStatusDto {
    Long getId();
    OrderStatusEnum getName();
    String getDescription();

}
