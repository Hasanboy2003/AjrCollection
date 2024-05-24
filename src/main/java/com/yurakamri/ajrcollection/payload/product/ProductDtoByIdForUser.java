package com.yurakamri.ajrcollection.payload.product;


import com.yurakamri.ajrcollection.payload.SizeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDtoByIdForUser {
    private UUID id;

    private String name;

    private String description;

    private String brandName;

    private BigDecimal Price;

    private BigDecimal discount;

    private BigDecimal SellingPrice;

    Set<SizeDto> sizeSet;

    Set<ColorAttachmentDto> colorAttachmentDtos;

    private boolean favorite;

}
