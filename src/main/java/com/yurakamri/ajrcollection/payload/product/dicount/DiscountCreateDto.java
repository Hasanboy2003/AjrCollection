package com.yurakamri.ajrcollection.payload.product.dicount;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiscountCreateDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "DISCOUNT START DATE MUST NOT BE NULL!")
    LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "DISCOUNT END DATE MUST NOT BE NULL!")
    LocalDate endDate;

    @NotNull(message = "DISCOUNT PERCENT MUST NOT BE NULL!")
    @DecimalMin(value = "0.0", inclusive = false, message = "MINIMUM DISCOUNT PERCENT MUST BE GREATER THAN 0.0")
    @DecimalMax(value = "100.0", inclusive = false, message = "MAXIMUM DISCOUNT PERCENT MUST BE LESS THAN 100.0")
    @Digits(integer = 2, fraction = 2, message = "PERCENT DOES NOT MATCH PATTERN : xx.xx")
    BigDecimal percent;

    List<UUID> productIdList;

}
