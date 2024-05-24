package com.yurakamri.ajrcollection.entity;

import com.yurakamri.ajrcollection.entity.abs.AbsUUIDEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Attachment extends AbsUUIDEntity {

    @Column(nullable = false, updatable = false)
    String originalName;

    @Column(nullable = false, updatable = false, unique = true)
    String generatedName;

    @Column(nullable = false, updatable = false)
    String contentType;

    @Column(nullable = false)
    long size;

    @Column(nullable = false, updatable = false)
    String fileLocation;

    public Attachment(String originalName, String contentType, long size) {
        this.originalName = originalName;
        this.contentType = contentType;
        this.size = size;
    }
}
