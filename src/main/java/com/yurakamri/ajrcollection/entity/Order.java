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
@Entity(name = "orders")
public class Order extends AbsUUIDEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    OrderStatus orderStatus;

    @ManyToOne(optional = false)
    User user;


    @ManyToOne
    User driver;

    String longitude;

    public Order(OrderStatus orderStatus, User user, String code, String phoneNumber) {
        this.orderStatus = orderStatus;
        this.user = user;
        this.code = code;
        this.phoneNumber = phoneNumber;
    }

    String latitude;
    boolean cash=true;
    BigDecimal shippingAmount; // DELIVERY SUM

    // PAY TYPE

    @Column(nullable = false, unique = true, updatable = false)
    String code;

    @Column(columnDefinition = "TEXT")
    String address;

    @Column(nullable = false)
    String phoneNumber;

    public Order(OrderStatus orderStatus, User user, String longitude, String latitude, BigDecimal shippingAmount, String code, String address, String phoneNumber) {
        this.orderStatus = orderStatus;
        this.user = user;
        this.longitude = longitude;
        this.latitude = latitude;
        this.shippingAmount = shippingAmount;
        this.code = code;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
