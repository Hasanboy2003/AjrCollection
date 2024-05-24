package com.yurakamri.ajrcollection.payload.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDtoForSales {

    private UUID id;

    private String name;
}
