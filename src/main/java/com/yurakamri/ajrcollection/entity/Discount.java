package com.yurakamri.ajrcollection.entity;

import com.yurakamri.ajrcollection.entity.abs.AbsLongEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Discount extends AbsLongEntity {

    @Column(nullable = false)
    LocalDate startDate;

    @Column(nullable = false)
    LocalDate endDate;

    @Column(nullable = false)
    BigDecimal percent;

    @OneToMany(mappedBy = "discount")
    List<Product> products;
    public Discount(LocalDate startDate, LocalDate endDate, BigDecimal percent) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.percent = percent;
    }


}
