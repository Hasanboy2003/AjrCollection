package com.yurakamri.ajrcollection.payload.product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDto {
    UUID id;
    @NotNull(message = "Mahsulot nomi bo`sh bo`lishi mumkin emas")
    @Size(min = 3, max = 255, message = "Mahsulot nomi 3-255 dona belgidan iborat bo`lishi kerak")
    String name;

    @NotNull
    Long brandId;

    UUID categoryId;

    boolean active = true;

    String description;

    BigDecimal outcomePrice;

    List<ColorAttachment> colorAttachmentList;

//    Discount discount;
}
