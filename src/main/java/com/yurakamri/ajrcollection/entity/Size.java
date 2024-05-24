package com.yurakamri.ajrcollection.entity;

import com.yurakamri.ajrcollection.entity.abs.AbsLongEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Size extends AbsLongEntity {

    @NotNull
    @Column(nullable = false, unique = true)
    String name;

    @Column(nullable = false, columnDefinition = "boolean default true")
    boolean active = true;
}
