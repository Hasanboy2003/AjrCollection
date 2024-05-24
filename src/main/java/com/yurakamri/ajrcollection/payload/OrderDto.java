package com.yurakamri.ajrcollection.payload;

import com.yurakamri.ajrcollection.entity.OrderStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
    UUID id;
    String orderStatus;
    String longitude;
    String latitude;
    UUID driverId;
    BigDecimal shippingAmount;
    String code;
    String address;
    @NotNull
    boolean cash;
    @NotNull
    String phoneNumber;
}
