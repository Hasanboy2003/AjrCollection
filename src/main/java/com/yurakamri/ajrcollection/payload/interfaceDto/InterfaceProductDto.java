package com.yurakamri.ajrcollection.payload.interfaceDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface InterfaceProductDto {
    UUID getId();
    String getName();
    String getBrandName();
    String getCategoryName();
    UUID getAttachmentId();
//    String getDescription();
//    String getCode();
//    LocalDate getDiscountStartDate();
//    LocalDate getDiscountEndDate();
//    BigDecimal getDiscountPercent();
}
