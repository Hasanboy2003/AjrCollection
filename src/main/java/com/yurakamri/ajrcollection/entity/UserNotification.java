package com.yurakamri.ajrcollection.entity;

import com.yurakamri.ajrcollection.entity.abs.AbsUUIDEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class UserNotification extends AbsUUIDEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    Notification notification;

    @Column(nullable = false)
    boolean seen = false;
}
