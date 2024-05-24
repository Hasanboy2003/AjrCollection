package com.yurakamri.ajrcollection.entity;

import com.yurakamri.ajrcollection.entity.abs.AbsUUIDEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Notification extends AbsUUIDEntity {

    @OneToOne(fetch = FetchType.LAZY)
    Attachment attachment;

    @Column(nullable = false)
    String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    String body;
}
