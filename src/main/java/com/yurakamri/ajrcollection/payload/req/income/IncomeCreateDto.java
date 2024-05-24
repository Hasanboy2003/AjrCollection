package com.yurakamri.ajrcollection.payload.req.income;

import com.yurakamri.ajrcollection.payload.req.incomeDetail.IncomeDetailCreateDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IncomeCreateDto {

    @NotNull(message = "DISTRIBUTOR ID MUST NOT BE NULL")
    Long distributorId;

    String description;

    @NotEmpty(message = "Income details must not be empty!")
    List<@Valid IncomeDetailCreateDto> incomeDetails;
}
