package com.yurakamri.ajrcollection.payload.req;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DistributorDto {

    @NotBlank(message = "DISTRIBUTOR NAME MUST NOT BE BLANK")
    String name;

    String description;

    @NotNull
    boolean active;
}
