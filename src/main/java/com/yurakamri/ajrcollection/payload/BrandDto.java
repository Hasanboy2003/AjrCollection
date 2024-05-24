package com.yurakamri.ajrcollection.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class BrandDto {

    Long id;

    @NotBlank(message = "Name must not be empty")
    @NotNull(message = "Brand nomi bo`sh bo`lishi mumkin emas")
    String name;

    boolean active = true;

    UUID logoId;

    Timestamp creatAt;
}
