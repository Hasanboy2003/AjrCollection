package com.yurakamri.ajrcollection.payload;

import com.yurakamri.ajrcollection.entity.Category;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDto {

    UUID id;

    @NotNull(message = "Category nomi bo`sh bo`lishi mumkin emas")
    @NotBlank(message = "Category nomi bo`sh bo`lishi mumkin emas")
    @Size(min = 2, message = "Category nomi 2 bekgidan kam bo`lmasligi kerak")
    String name;

    String description;

    boolean active = true;

    UUID parentCategoryId;
}
