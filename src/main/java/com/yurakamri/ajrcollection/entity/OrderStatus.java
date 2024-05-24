package com.yurakamri.ajrcollection.entity;

import com.yurakamri.ajrcollection.entity.abs.AbsLongEntity;
import com.yurakamri.ajrcollection.entity.enums.OrderStatusEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class OrderStatus extends AbsLongEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, updatable = false)
    OrderStatusEnum name;

    @Column(columnDefinition = "TEXT")
    String description;

}
