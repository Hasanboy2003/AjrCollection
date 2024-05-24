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
public class ColorAttachmentDto {
    private Long id;
    private String colorCode;
    private Set<UUID> attachmentIdList;
    public ColorAttachmentDto(Long id, Set<UUID> attachmentIdList) {
        this.id = id;
        this.attachmentIdList = attachmentIdList;
    }
}
