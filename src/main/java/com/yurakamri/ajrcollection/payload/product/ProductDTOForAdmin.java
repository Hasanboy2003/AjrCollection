package com.yurakamri.ajrcollection.payload.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor 
@NoArgsConstructor
public class ProductDTOForAdmin {

    private UUID id;
    private String name;
    private Long brandId;
    private BigDecimal price;
    private Integer amount;
    private LocalDateTime creatAt;
    private boolean active;
    private UUID attachmentId;

}
