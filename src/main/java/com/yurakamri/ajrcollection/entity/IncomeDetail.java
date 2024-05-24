package com.yurakamri.ajrcollection.entity;

import com.yurakamri.ajrcollection.entity.abs.AbsUUIDEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class IncomeDetail extends AbsUUIDEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    Size size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    ProductColor productColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    Income income;

    @Column(nullable = false)
    Integer quantity;

    @Column(nullable = false)
    BigDecimal price;

}
