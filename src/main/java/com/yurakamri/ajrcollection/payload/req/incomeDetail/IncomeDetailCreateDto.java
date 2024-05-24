package com.yurakamri.ajrcollection.payload.req.incomeDetail;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IncomeDetailCreateDto {

    UUID incomeId;

    @NotNull(message = "PRODUCT ID MUST NOT BE NULL")
    UUID productId;

    @Min(value = 1, message = "ID FIELD CANNOT BE LESS THAN 1")
    @NotNull(message = "COLOR ID MUST NOT BE NULL")
    Long colorId;

    @Min(value = 1, message = "ID FIELD CANNOT BE LESS THAN 1")
    @NotNull(message = "SIZE ID MUST NOT BE NULL")
    Long sizeId;

    @Min(value = 1, message = "PRODUCT QUANTITY CAN BE AT LEAST 1")
    @NotNull(message = "QUANTITY MUST NOT BE NULL")
    Integer quantity;

    @NotNull(message = "PRODUCT INCOME PRICE MUST NOT BE NULL")
    @DecimalMin(value = "0.0", inclusive = false, message = "PRICE MUST BE GREATER THAN 0.0")
    @Digits(integer = 10, fraction = 4, message = "BIG DECIMAL NUMBER IS INVALID FOR PRICE")
    BigDecimal price;
}
