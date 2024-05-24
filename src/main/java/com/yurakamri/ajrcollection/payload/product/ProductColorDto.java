package com.yurakamri.ajrcollection.payload.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductColorDto {

    private UUID id;

    private String name;

    private String code;

    private Set<ColorDtoForIncome> list;
}
