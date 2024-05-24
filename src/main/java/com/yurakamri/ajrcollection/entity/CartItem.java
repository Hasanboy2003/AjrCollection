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
public class CartItem extends AbsUUIDEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    ProductColor productColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    Size size;

    @Column(nullable = false)
    Integer quantity;

    @ManyToOne
    Discount discount;

    BigDecimal fixedPrice;

    @ManyToOne
    Order order;

    @Column(nullable = false)
    boolean forCharity;

    public CartItem(User user, ProductColor productColor, Size size, Integer quantity, boolean forCharity) {
        this.user = user;
        this.productColor = productColor;
        this.size = size;
        this.quantity = quantity;
        this.forCharity = forCharity;
    }


}
