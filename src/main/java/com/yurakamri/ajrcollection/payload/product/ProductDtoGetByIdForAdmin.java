package com.yurakamri.ajrcollection.payload.product;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDtoGetByIdForAdmin {

    private UUID id;

    private String name;

    private String description;

    private Long brandId;

    private UUID categoryId;

    private Set<ColorAttachmentDto> colorAttachmentDtoList;

    private boolean active;
    private String size;

    private Set<ProductColorSizeAmount> productColorSizeAmountList;

    public ProductDtoGetByIdForAdmin(UUID id, String name, String description, Long brandId, UUID categoryId, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.active = active;
    }


    /*
        * 1. ID
         * 2. Name
         * 3. description
         * 4. Brand
         * 5. Category
         * 7. Color  ===== rasm
         * 8. aktivligi
         * 9. Miqdor
         * **/

}
