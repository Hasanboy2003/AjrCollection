package com.yurakamri.ajrcollection.payload;

import com.yurakamri.ajrcollection.entity.ProductColor;
import com.yurakamri.ajrcollection.entity.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDto {
    UUID id;
    @NotNull
    UUID productId;
    @NotNull
    Long colorId;
    @NotNull
    Long sizeId;
    @NotNull
    Integer quantity;
    @NotNull
    BigDecimal fixedPrice;
    @NotNull
    boolean forCharity;
}
