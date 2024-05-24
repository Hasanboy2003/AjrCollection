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
public class Product extends AbsUUIDEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    Discount discount;

    @Column(unique = true, nullable = false)
    String name;

    @Column(nullable = false)
    boolean active = true;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(nullable = false)
    BigDecimal outcomePrice;

    @Column(nullable = false, unique = true, updatable = false)
    String code;

    public Product(String name, Brand brand, Category category, boolean active, String description,
                   BigDecimal outcomePrice, String code, Discount discount) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.active = active;
        this.description = description;
        this.outcomePrice = outcomePrice;
        this.code = code;
        this.discount = discount;
    }
}
