package com.yurakamri.ajrcollection.payload.product;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllProductDtoForUser {

    private UUID id;

    private String name;

    private UUID attachmentId;

    private BigDecimal price;

    private BigDecimal discount;

    private BigDecimal sellingPrice;

    private String brandName;

    private boolean favourite;
}
