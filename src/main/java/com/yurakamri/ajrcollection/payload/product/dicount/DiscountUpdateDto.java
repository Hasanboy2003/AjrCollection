package com.yurakamri.ajrcollection.payload.product.dicount;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiscountUpdateDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "DISCOUNT START DATE MUST NOT BE NULL!")
    LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "DISCOUNT END DATE MUST NOT BE NULL!")
    LocalDate endDate;

    @NotNull(message = "DISCOUNT PERCENT MUST NOT BE NULL!")
    @Min(value = 1, message = "DISCOUNT CAN BE AT LEAST 1% !")
    @Max(value = 100, message = "DISCOUNT CAN BE MAXIMUM 100% !")
    BigDecimal percent;
}
