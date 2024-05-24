package com.yurakamri.ajrcollection.projection;

import java.math.BigDecimal;
import java.util.UUID;

public interface ViewProductSearch {

    UUID getId();

    String getName();

    UUID getAttachmentId();

    BigDecimal getPrice();

    BigDecimal getSellingPrice();

    BigDecimal getDiscount();

    String getBrandName();

    Boolean getFavourite();
}
