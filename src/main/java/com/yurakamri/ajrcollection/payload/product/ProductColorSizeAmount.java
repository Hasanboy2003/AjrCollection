package com.yurakamri.ajrcollection.payload.product;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductColorSizeAmount {

    private Long colorId;

    private Long sizeId;

    private Integer amount;
}
