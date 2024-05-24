package com.yurakamri.ajrcollection.payload.req.income;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IncomeUpdateDto {

    @NotNull(message = "DISTRIBUTOR ID MUST NOT BE NULL")
    Long distributorId;

    String description;
}
