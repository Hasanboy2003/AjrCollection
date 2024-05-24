package com.yurakamri.ajrcollection.payload.userForAdmin;

import com.yurakamri.ajrcollection.payload.product.ColorAttachmentDto;
import com.yurakamri.ajrcollection.payload.product.ProductDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailDto {
    UUID id;
    @NotNull
    UUID productId;
    String productName;
    String productDescription;
    BigDecimal discountPrice;
    @NotNull
    String size;
    String brandName;
    @NotNull
    Integer quantity;
    @NotNull
    BigDecimal fixedPrice;
    @NotNull
    boolean forCharity;
    Set<ColorAttachmentDto> colorAttachments;
}
