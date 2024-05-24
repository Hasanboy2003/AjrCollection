package com.yurakamri.ajrcollection.payload.home;

import com.yurakamri.ajrcollection.payload.product.AllProductDtoForUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandProductDto {
    private Long brandId;
    private String brandName;
    private List<AllProductDtoForUser> allProductDtoForUserList;
}
