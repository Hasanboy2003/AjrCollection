package com.yurakamri.ajrcollection.payload.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSearchDto {

    @NotNull(message = "search key must not be null")
    String searchText;

    @NotNull
    @Min(value = 1, message = "search min limit is 1")
    Integer limit;
}
