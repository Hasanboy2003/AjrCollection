package com.yurakamri.ajrcollection.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDiscountDto {
    Long id;
    UUID productId;
    LocalDate startDate;
    LocalDate endDate;
    BigDecimal percent;
    BigDecimal productPrice;
    BigDecimal sellingPrice;
    String productName;
    String brandName;
    boolean active;
}
