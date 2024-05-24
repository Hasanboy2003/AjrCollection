package com.yurakamri.ajrcollection.entity;

import com.yurakamri.ajrcollection.entity.abs.AbsUUIDEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class ChangePhoneNumber extends AbsUUIDEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    User user;

    @Column(nullable = false, updatable = false)
    String tempPhoneNumber;

    @Column(nullable = false, updatable = false)
    String code;

    boolean verified;

    boolean active = true;
}
